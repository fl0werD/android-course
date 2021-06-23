package com.example.fl0wer.domain.directions

import com.example.fl0wer.domain.core.dispatchers.DispatchersProvider
import kotlinx.coroutines.withContext

class DirectionInteractorImpl(
    private val directionRepository: DirectionRepository,
    private val dispatchersProvider: DispatchersProvider,
) : DirectionInteractor {
    override suspend fun route(origin: String, destination: String) =
        withContext(dispatchersProvider.io) {
            directionRepository.getRoute(origin, destination)
        }
}
