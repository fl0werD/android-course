package com.example.fl0wer.androidApp.data.locations

import com.example.fl0wer.androidApp.data.locations.LocationMapper.toEntity
import com.example.fl0wer.androidApp.data.locations.LocationMapper.toLocation
import com.example.fl0wer.androidApp.data.locations.database.LocationDao
import com.example.fl0wer.androidApp.data.locations.database.LocationDatabase
import com.example.fl0wer.domain.contacts.Contact
import com.example.fl0wer.domain.locations.Location
import com.example.fl0wer.domain.locations.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl(
    private val locationDao: LocationDao,
) : LocationRepository {
    override suspend fun location(contactId: Int): Location {
        return locationDao.loadLocation(contactId).toLocation()
    }

    override suspend fun locations(): List<Location> {
        return locationDao.loadLocations().toLocation()
    }

    override suspend fun save(location: Location) {
        locationDao.insert(location.toEntity())
    }
}
