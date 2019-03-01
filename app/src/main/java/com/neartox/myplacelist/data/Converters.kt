package com.neartox.myplacelist.data

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


class Converters {
  @TypeConverter
  fun listStringToJSon(list: List<String>): String {
    val gsonBuilder = GsonBuilder().serializeNulls()
    val gson = gsonBuilder.create()
    return gson.toJson(list)
  }
  @TypeConverter
  fun listPhotoToJSon(list: List<Photo>): String {
    val gsonBuilder = GsonBuilder().serializeNulls()
    val gson = gsonBuilder.create()
    return gson.toJson(list)
  }

  @TypeConverter
  fun geometryToJSon(value: Geometry): String {
    val gsonBuilder = GsonBuilder().serializeNulls()
    val gson = gsonBuilder.create()
    return gson.toJson(value)
  }

  @TypeConverter
  fun jsonToListString(value: String): List<String> {
    val gsonBuilder = GsonBuilder().serializeNulls()
    val gson = gsonBuilder.create()
    val listType = object : TypeToken<List<String>>() {}.type
    return gson.fromJson(value, listType)
  }

  @TypeConverter
  fun jsonToListPhoto(value: String): List<Photo> {
    val gsonBuilder = GsonBuilder().serializeNulls()
    val gson = gsonBuilder.create()
    val listType = object : TypeToken<List<Photo>>() {}.type
    return gson.fromJson(value, listType)
  }


  @TypeConverter
  fun jsonToGeometry(value: String): Geometry {
    val gsonBuilder = GsonBuilder().serializeNulls()
    val gson = gsonBuilder.create()
    val listType = object : TypeToken<Geometry>() {}.type
    return gson.fromJson(value, listType)
  }

}