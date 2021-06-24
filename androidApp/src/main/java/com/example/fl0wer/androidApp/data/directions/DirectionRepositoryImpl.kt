package com.example.fl0wer.androidApp.data.directions

import com.example.fl0wer.androidApp.data.core.network.GoogleApi
import com.example.fl0wer.domain.directions.DirectionRepository
import com.example.fl0wer.domain.directions.Route
import com.example.fl0wer.domain.locations.LatLon
import com.google.maps.android.PolyUtil

class DirectionRepositoryImpl(
    private val googleApi: GoogleApi,
) : DirectionRepository {
    override suspend fun getRoute(
        origin: String,
        destination: String,
    ): Route? {
        val response = googleApi.getRoute(origin, destination)
        if (response.status != "OK") {
            return null
        }
        val route = response.routes.firstOrNull() ?: return null
        val points = response.routes
            .firstOrNull()
            ?.legs
            ?.firstOrNull()
            ?.steps
            ?.fold(emptyList<LatLon>()) { result, step ->
                result.plus(
                    PolyUtil.decode(step.polyline.points).map {
                        LatLon(it.latitude, it.longitude)
                    }
                )
            } ?: return null
        return Route(
            route.bounds.toBound(),
            points,
        )
    }
}
