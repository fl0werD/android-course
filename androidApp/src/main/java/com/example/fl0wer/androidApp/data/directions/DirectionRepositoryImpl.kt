package com.example.fl0wer.androidApp.data.directions

import com.example.fl0wer.androidApp.data.directions.network.DirectionsApi
import com.example.fl0wer.domain.directions.Direction
import com.example.fl0wer.domain.directions.DirectionRepository

class DirectionRepositoryImpl(
    private val directionsApi: DirectionsApi,
) : DirectionRepository {
    override suspend fun getRoute(
        origin: String,
        destination: String,
    ): Direction? {
        val response = directionsApi.getRoute(origin, destination)
        if (response.status == "OK") {
            return Direction(true)
        } else {
            return null
        }
    }
}
