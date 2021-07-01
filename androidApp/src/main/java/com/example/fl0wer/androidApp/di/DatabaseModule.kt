package com.example.fl0wer.androidApp.di

import android.content.Context
import androidx.room.Room
import com.example.fl0wer.BuildConfig
import com.example.fl0wer.androidApp.data.locations.database.LocationDao
import com.example.fl0wer.androidApp.data.locations.database.LocationDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {
    @[Provides Singleton]
    fun provideContactDatabase(
        context: Context,
    ): LocationDatabase = Room.databaseBuilder(
        context,
        LocationDatabase::class.java,
        BuildConfig.DB_NAME,
    ).build()

    @Provides
    fun provideLocationDao(
        locationDatabase: LocationDatabase
    ): LocationDao = locationDatabase.locationDao()
}
