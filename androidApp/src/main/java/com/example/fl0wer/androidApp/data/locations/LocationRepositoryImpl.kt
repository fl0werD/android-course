package com.example.fl0wer.androidApp.data.locations

import com.example.fl0wer.androidApp.data.core.network.GoogleApi
import com.example.fl0wer.androidApp.data.locations.LocationMapper.toEntity
import com.example.fl0wer.androidApp.data.locations.LocationMapper.toLocation
import com.example.fl0wer.androidApp.data.locations.database.LocationDao
import com.example.fl0wer.androidApp.domain.core.Result
import com.example.fl0wer.androidApp.ui.core.dispatchers.DispatchersProvider
import com.example.fl0wer.androidApp.domain.locations.Location
import com.example.fl0wer.androidApp.domain.locations.LocationRepository
import com.example.fl0wer.androidApp.util.isSuccess
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

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

    override suspend fun save(location: Location) =
        withContext(dispatchersProvider.io) {
            locationDao.insert(location.toEntity())
        }

    override suspend fun reverseGeocode(latitude: Double, longitude: Double) =
        withContext(dispatchersProvider.io) {
            val response = googleApi.reverseGeocode("$latitude,$longitude")
            if (response.isSuccess()) {
                Result.Success(response.results[0].address)
            } else {
                Result.Failure(IOException("Status not OK"))
            }
        }
}
