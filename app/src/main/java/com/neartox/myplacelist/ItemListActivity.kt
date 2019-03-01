package com.neartox.myplacelist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.snackbar.Snackbar
import com.neartox.myplacelist.data.Maps
import com.neartox.myplacelist.sevices.AppDatabase
import com.neartox.myplacelist.sevices.MapViewModel
import com.neartox.myplacelist.sevices.MapViewModelFactory
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity() {
  private var twoPane: Boolean = false

  private lateinit var appData: AppDatabase

  override fun onCreate(savedInstanceState: Bundle?) {
    Fresco.initialize(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_item_list)

    setSupportActionBar(toolbar)
    toolbar.title = title

    fab.setOnClickListener { view ->
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show()
    }

    if (item_detail_container != null) {
      // The detail container view will be present only in the
      // large-screen layouts (res/values-w900dp).
      // If this view is present, then the
      // activity should be in two-pane mode.
      twoPane = true
    }
    appData = AppDatabase.getInstance(this)

    setupRecyclerView(item_list)
  }

  private fun setupRecyclerView(recyclerView: RecyclerView) {
    recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, twoPane)
  }

  class SimpleItemRecyclerViewAdapter(
    private val parentActivity: ItemListActivity,
    private val twoPane: Boolean
  ) :
    RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener
    private var mapList: List<Maps> = emptyList()


    init {
      val viewModel = ViewModelProviders.of(
        parentActivity,
        MapViewModelFactory(parentActivity)
      ).get(MapViewModel::class.java)
      viewModel.mapsList.observe(parentActivity, Observer {
        mapList = it
        notifyDataSetChanged()
      })
      viewModel.loadData()

      onClickListener = View.OnClickListener { v ->
        val item = v.tag as Maps
        if (twoPane) {
          val fragment = ItemDetailFragment().apply {
            arguments = Bundle().apply {
              putString(ItemDetailFragment.ARG_ITEM_ID, item.id)
            }
          }
          parentActivity.supportFragmentManager
            .beginTransaction()
            .replace(R.id.item_detail_container, fragment)
            .commit()
        } else {
          val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
            putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id)
          }
          v.context.startActivity(intent)
        }
      }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.item_list_content, parent, false)
      return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val item = mapList[position]
      holder.imageView.setImageURI(item.icon)
      holder.nameView.text = item.name

      with(holder.itemView) {
        tag = item
        setOnClickListener(onClickListener)
      }
    }

    override fun getItemCount() = mapList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
      val imageView: SimpleDraweeView = view.id_item_image
      val nameView: TextView = view.id_item_name
    }
  }
}
