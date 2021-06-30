package com.example.fl0wer.androidApp.domain.locations

import com.example.fl0wer.androidApp.domain.core.Result
import com.example.fl0wer.androidApp.domain.core.dispatchers.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocationInteractorImpl(
    private val locationRepository: LocationRepository,
    private val dispatchersProvider: DispatchersProvider,
) : LocationInteractor {
    override fun observeLocation(contactId: Int): Flow<Location?> {
        return locationRepository.observeLocation(contactId)
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    override suspend fun mapClicked(contactId: Int, latitude: Double, longitude: Double): Unit =
        withContext(dispatchersProvider.io) {
            try {
                when (val result = locationRepository.reverseGeocode(latitude, longitude)) {
                    is Result.Success -> {
                        locationRepository.save(
                            Location(
                                contactId,
                                latitude,
                                longitude,
                                result.value,
                            )
                        )
                    }
                    is Result.Failure -> {
                        result.throwable
                    }
                }
            } catch (e: Exception) {
                // throw e
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
