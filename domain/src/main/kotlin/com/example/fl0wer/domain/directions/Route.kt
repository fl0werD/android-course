package com.example.fl0wer.domain.directions

import com.example.fl0wer.domain.locations.LatLon

data class Route(
    val bounds: Bounds,
    val points: List<LatLon>,
)

data class Bounds(
    val northeast: LatLon,
    val southwest: LatLon,
)
