package com.neartox.myplacelist.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MapsDao {
  @Query("SELECT * FROM maps ORDER BY name")
  fun getMaps(): LiveData<List<Maps>>

  @Query("SELECT * FROM maps WHERE id = :mapsID")
  fun getMap(mapsID: String): LiveData<Maps>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(maps: List<Maps>)
  
}