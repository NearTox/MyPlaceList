package com.neartox.myplacelist.data

data class MapsList(
  val results: List<Maps> = ArrayList(),
  val status: String = "Error"
)