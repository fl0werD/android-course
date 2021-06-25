package com.example.fl0wer.androidApp.data.directions

import com.example.fl0wer.androidApp.data.core.network.GoogleApi
import com.example.fl0wer.domain.core.Result
import com.example.fl0wer.domain.directions.DirectionRepository
import com.example.fl0wer.domain.directions.Route
import com.example.fl0wer.domain.locations.LatLon
import com.google.maps.android.PolyUtil
import java.io.IOException

class DirectionRepositoryImpl(
    private val googleApi: GoogleApi,
) : DirectionRepository {
    override suspend fun getRoute(
        origin: String,
        destination: String,
    ): Result<Route> {
        val response = googleApi.getRoute(origin, destination)
        if (response.status != "OK") {
            return Result.Failure(IOException("Status not OK"))
        }
        val route = response.routes.firstOrNull()
        if (route == null) {
            return Result.Failure(IOException("No routes"))
        }
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
            }
        if (points == null) {
            return Result.Failure(IOException("No points"))
        }
        return Result.Success(
            Route(route.bounds.toBound(), points)
        )
    }
}
