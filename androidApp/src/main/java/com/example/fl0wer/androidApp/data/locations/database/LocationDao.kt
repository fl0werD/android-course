package com.example.fl0wer.androidApp.data.locations.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM contacts")
    fun loadLocations(): List<LocationEntity>

    @Query("SELECT * FROM contacts WHERE id = :id LIMIT 1")
    fun loadLocation(id: Int): LocationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(locationEntity: LocationEntity)
}
