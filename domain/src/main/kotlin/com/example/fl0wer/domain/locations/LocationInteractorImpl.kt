package com.example.fl0wer.domain.locations

import com.example.fl0wer.domain.core.dispatchers.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.IOException

class LocationInteractorImpl(
    private val locationRepository: LocationRepository,
    private val dispatchersProvider: DispatchersProvider,
) : LocationInteractor {
    override fun observeLocation(contactId: Int): Flow<Location?> {
        return locationRepository.observeLocation(contactId)
    }

    override suspend fun mapClicked(contactId: Int, latitude: Double, longitude: Double) =
        withContext(dispatchersProvider.io) {
            try {
                val address = locationRepository.reverseGeocode(latitude, longitude)
                    ?: throw IOException("Address null")
                locationRepository.save(
                    Location(
                        contactId,
                        latitude,
                        longitude,
                        address
                    )
                )
            } catch (e: Exception) {
                throw e
            }
        }

    /*override suspend fun location(contactId: Int) =
        withContext(dispatchersProvider.io) {
            locationRepository.location(contactId)
        }*/

    override suspend fun locations() =
        withContext(dispatchersProvider.io) {
            locationRepository.locations()
        }
}
