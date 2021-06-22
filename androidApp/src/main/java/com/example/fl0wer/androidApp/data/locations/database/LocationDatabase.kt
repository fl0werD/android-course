package com.example.fl0wer.androidApp.data.locations.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fl0wer.BuildConfig

@Database(
    entities = [
        LocationEntity::class,
    ],
    version = BuildConfig.DB_VERSION,
    exportSchema = false,
)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}
