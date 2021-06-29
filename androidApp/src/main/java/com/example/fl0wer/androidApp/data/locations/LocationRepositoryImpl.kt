package com.example.fl0wer.androidApp.data.locations

import com.example.fl0wer.androidApp.data.core.network.GoogleApi
import com.example.fl0wer.androidApp.data.locations.LocationMapper.toEntity
import com.example.fl0wer.androidApp.data.locations.LocationMapper.toLocation
import com.example.fl0wer.androidApp.data.locations.database.LocationDao
import com.example.fl0wer.domain.core.Result
import com.example.fl0wer.domain.core.dispatchers.DispatchersProvider
import com.example.fl0wer.domain.locations.Location
import com.example.fl0wer.domain.locations.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException

class LocationRepositoryImpl(
    private val locationDao: LocationDao,
    private val googleApi: GoogleApi,
    private val dispatchersProvider: DispatchersProvider,
) : LocationRepository {
    override fun observeLocation(contactId: Int): Flow<Location?> {
        return locationDao.loadLocation(contactId).map {
            it?.toLocation()
        }
    }

    override suspend fun locations(): List<Location> =
        withContext(dispatchersProvider.io) {
            locationDao.loadLocations().toLocation()
        }

    override suspend fun save(location: Location) {
        locationDao.insert(location.toEntity())
    }

    override suspend fun reverseGeocode(latitude: Double, longitude: Double): Result<String> {
        val response = googleApi.reverseGeocode("$latitude,$longitude")
        return if (response.status == "OK") {
            Result.Success(response.results[0].address)
        } else {
            Result.Failure(IOException("Status not OK"))
        }
    }
}
