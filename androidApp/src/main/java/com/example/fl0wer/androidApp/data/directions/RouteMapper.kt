package com.example.fl0wer.androidApp.data.directions

import com.example.fl0wer.androidApp.data.directions.LatLonMapper.toLatLon
import com.example.fl0wer.androidApp.data.directions.LatLonMapper.toParcelable
import com.example.fl0wer.androidApp.data.directions.network.BoundsDto
import com.example.fl0wer.androidApp.data.directions.network.LatLonDto
import com.example.fl0wer.androidApp.domain.directions.Bounds
import com.example.fl0wer.androidApp.domain.directions.Route
import com.example.fl0wer.androidApp.domain.locations.LatLon

fun BoundsDto.toBound() = Bounds(
    northeast = northeast.toLatLon(),
    southwest = southwest.toLatLon(),
)

fun Bounds.toParcelable() = BoundsParcelable(
    northeast = northeast.toParcelable(),
    southwest = southwest.toParcelable(),
)

fun Route.toParcelable() = RouteParcelable(
    bounds = bounds.toParcelable(),
    points = points.toParcelable(),
)

object LatLonMapper {
    fun LatLonDto.toLatLon() = LatLon(
        latitude = latitude,
        longitude = longitude,
    )

    fun LatLon.toParcelable() = LatLonParcelable(
        latitude = latitude,
        longitude = longitude,
    )

    fun List<LatLon>.toParcelable(): List<LatLonParcelable> = map { it.toParcelable() }
}
