package com.neartox.myplacelist.sevices

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.neartox.myplacelist.data.Maps
import com.neartox.myplacelist.data.MapsDao
import com.neartox.myplacelist.data.MapsList
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException

class MapViewModel(context: Context) : ViewModel() {

  private val mapsDao: MapsDao = AppDatabase.getInstance(context).mapsDao()
  val mapsList: LiveData<List<Maps>>
  val mapStatus: MutableLiveData<String> = MutableLiveData()

  init {
    mapsList = mapsDao.getMaps()
  }

  interface NearBySearchService {
    @GET("maps/api/place/nearbysearch/json?location=19.4325987,-99.1419372&radius=1000&types=food")
    fun nearbysearch(@Query("key") key: String): Call<MapsList>
  }

  interface DetailsService {
    @GET("maps/api/place/details/json")
    fun getdetail(
      @Query("key") user: String,
      @Query("placeid") placeid: String
    ): Call<MapsList>
  }

  fun loadData() {
    val task = @SuppressLint("StaticFieldLeak")
    object : AsyncTask<Void, Void, MapsList>() {
      override fun doInBackground(vararg voids: Void): MapsList {
        var list: MapsList = MapsList()

        // Create a very simple REST adapter which points the GitHub API.
        val retrofit = Retrofit.Builder()
          .baseUrl(API_URL)
          .addConverterFactory(GsonConverterFactory.create())
          .build()

        // Create an instance of our GitHub API interface.
        val service = retrofit.create(NearBySearchService::class.java)
        val call = service.nearbysearch(API_KEY)

        // Fetch and print a list of the contributors to the library.

        try {
          val body = call.execute().body()
          list = body!!
        } catch (e: IOException) {
          e.printStackTrace()
        }
        return list
      }

      override fun onPostExecute(data: MapsList) {
        mapStatus.value = data.status
        if (data.status == "OK") {
          mapsDao.insertAll(data.results)
        }
      }
    }
    task.execute()
  }

  companion object {
    private const val API_URL = "https://maps.googleapis.com/"
    private const val API_KEY = "AIzaSyBlTZ7-JAKxvHw8nyDI81USViBMSw6muhU"
  }
}