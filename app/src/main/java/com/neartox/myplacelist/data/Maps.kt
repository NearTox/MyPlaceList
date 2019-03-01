package com.neartox.myplacelist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "maps")
data class Maps(
  val geometry: Geometry,
  val icon: String,
  @PrimaryKey @ColumnInfo(name = "id") val id: String,
  val name: String,
  val photos: List<Photo>,
  val place_id: String,
  val reference: String,
  val scope: String,
  val types: List<String>,
  val vicinity: String
)

data class Geometry(
  val location: Location,
  val viewport: Viewport
)

data class Location(
  val lat: Double,
  val lng: Double
) {
  fun get(): String = "$lat,$lng"
}

data class Viewport(
  val northeast: Northeast,
  val southwest: Southwest
)

data class Southwest(
  val lat: Double,
  val lng: Double
)

data class Northeast(
  val lat: Double,
  val lng: Double
)

data class Photo(
  val height: Int,
  val html_attributions: List<String>,
  val photo_reference: String,
  val width: Int
)