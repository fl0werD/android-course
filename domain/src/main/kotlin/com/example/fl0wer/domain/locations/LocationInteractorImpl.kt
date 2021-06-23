package com.example.fl0wer.domain.locations

import com.example.fl0wer.domain.core.dispatchers.DispatchersProvider
import kotlinx.coroutines.withContext

class LocationInteractorImpl(
    private val locationRepository: LocationRepository,
    private val dispatchersProvider: DispatchersProvider,
) : LocationInteractor {
    override suspend fun mapClicked(contactId: Int, latitude: Double, longitude: Double) =
        withContext(dispatchersProvider.io) {
            try {
                val address = locationRepository.reverseGeocode(latitude, longitude)
                locationRepository.save(Location(contactId, latitude, longitude, address))
            } catch (e: Exception) {
                throw e
            }
        }

    override suspend fun location(contactId: Int) =
        withContext(dispatchersProvider.io) {
            locationRepository.location(contactId)
        }

    override suspend fun locations() =
        withContext(dispatchersProvider.io) {
            locationRepository.locations()
        }
}
