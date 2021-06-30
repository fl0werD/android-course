package com.example.fl0wer.androidApp.domain.directions

import com.example.fl0wer.androidApp.domain.locations.LatLon

data class Route(
    val bounds: Bounds,
    val points: List<LatLon>,
)

data class Bounds(
    val northeast: LatLon,
    val southwest: LatLon,
)
