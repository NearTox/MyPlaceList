package com.neartox.myplacelist.sevices

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.GsonBuilder
import com.neartox.myplacelist.data.DEMO_DATA_FILENAME
import com.neartox.myplacelist.data.MapsList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope


class SeedDatabaseWorker(
  context: Context,
  workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

  override val coroutineContext = Dispatchers.IO

  override suspend fun doWork(): Result = coroutineScope {

    try {
      applicationContext.assets.open(DEMO_DATA_FILENAME).use { inputStream ->
        inputStream.reader().use { jsonReader ->
          val gsonBuilder = GsonBuilder().serializeNulls()
          val gson = gsonBuilder.create()

          val json = gson.fromJson(jsonReader, MapsList::class.java)

          val database = AppDatabase.getInstance(applicationContext)
          database.mapsDao().insertAll(json.results)

          Result.success()
        }
      }
    } catch (ex: Exception) {
      Log.e(Companion.TAG, "Error seeding database", ex)
      Result.failure()
    }
  }

  companion object {
    private const val TAG = "SeedDatabaseWorker"
  }
}