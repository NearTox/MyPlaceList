package com.neartox.myplacelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.neartox.myplacelist.data.Maps
import com.neartox.myplacelist.sevices.AppDatabase
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.item_detail.*
import kotlinx.android.synthetic.main.item_detail.view.*

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */
class ItemDetailFragment : Fragment(), OnMapReadyCallback {
  private var mMap: GoogleMap? = null
  private var item: Maps? = null

  init {
    retainInstance = true
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Load the dummy content specified by the fragment
    // arguments. In a real-world scenario, use a Loader
    // to load content from a content provider.
    val itemId = arguments?.getString(ARG_ITEM_ID) ?: ""

    if (itemId != "") {
      AppDatabase.getInstance(context!!).mapsDao().getMap(itemId).observe(this, Observer {
        item = it

        // Add a marker in Sydney and move the camera
        val sydney = item?.geometry?.location?.run { LatLng(lat, lng) } ?: LatLng(-34.0, 151.0)

        mMap?.addMarker(MarkerOptions().position(sydney).title(item?.name))
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f))

        activity?.toolbar?.title = item?.name

        item_detail.text = item?.vicinity
      })
    }

  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val rootView = inflater.inflate(R.layout.item_detail, container, false)


    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    val mapView: MapView = rootView.id_item_map

    mapView.onCreate(savedInstanceState)
    mapView.onResume()
    mapView.getMapAsync(this)

    return rootView
  }


  /**
   * Manipulates the map once available.
   * This callback is triggered when the map is ready to be used.
   * This is where we can add markers or lines, add listeners or move the camera. In this case,
   * we just add a marker near Sydney, Australia.
   * If Google Play services is not installed on the device, the user will be prompted to install
   * it inside the SupportMapFragment. This method will only be triggered once the user has
   * installed Google Play services and returned to the app.
   */
  override fun onMapReady(googleMap: GoogleMap) {
    mMap = googleMap

    // Add a marker in Sydney and move the camera
    val sydney = item?.geometry?.location?.run { LatLng(lat, lng) } ?: LatLng(-34.0, 151.0)

    mMap?.addMarker(MarkerOptions().position(sydney).title(item?.name))
    mMap?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
  }

  companion object {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    const val ARG_ITEM_ID = "item_id"
  }
}
