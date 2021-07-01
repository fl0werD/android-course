package com.example.fl0wer.androidApp.data.directions.network

import com.google.gson.annotations.SerializedName

data class DirectionsResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("routes")
    val routes: List<RouteDto>,
)
