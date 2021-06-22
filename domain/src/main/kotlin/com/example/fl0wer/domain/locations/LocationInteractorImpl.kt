package com.example.fl0wer.domain.locations

import com.example.fl0wer.domain.core.dispatchers.DispatchersProvider
import kotlinx.coroutines.withContext

class LocationInteractorImpl(
    private val locationRepository: LocationRepository,
    private val dispatchersProvider: DispatchersProvider,
) : LocationInteractor {
    override suspend fun mapClicked(location: Location) {
        locationRepository.save(location)
    }

    override suspend fun location(contactId: Int) =
        withContext(dispatchersProvider.default) {
            locationRepository.location(contactId)
        }

    override suspend fun locations() =
        withContext(dispatchersProvider.default) {
            locationRepository.locations()
        }
}
