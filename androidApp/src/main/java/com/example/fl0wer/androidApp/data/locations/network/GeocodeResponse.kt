package com.example.fl0wer.androidApp.data.locations.network

import com.google.gson.annotations.SerializedName

data class GeocodeResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("results")
    val results: List<GeocodeResult>,
)

data class GeocodeResult(
    @SerializedName("formatted_address")
    val address: String,
)
