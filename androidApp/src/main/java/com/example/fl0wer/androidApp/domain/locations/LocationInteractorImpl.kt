package com.example.fl0wer.androidApp.domain.locations

import com.example.fl0wer.androidApp.domain.core.Result
import com.example.fl0wer.androidApp.ui.core.dispatchers.DispatchersProvider
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber

class LocationInteractorImpl(
    private val locationRepository: LocationRepository,
    private val dispatchersProvider: DispatchersProvider,
) : LocationInteractor {
    override fun observeLocation(contactId: Int): Flow<Location?> {
        return locationRepository.observeLocation(contactId)
    }

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
            } catch (e: IOException) {
                Timber.e(e)
            }
        }

    override suspend fun locations() =
        withContext(dispatchersProvider.io) {
            locationRepository.locations()
        }
}
