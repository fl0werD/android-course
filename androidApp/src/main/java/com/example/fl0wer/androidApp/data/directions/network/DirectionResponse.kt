package com.example.fl0wer.androidApp.data.directions.network

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

data class DirectionResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("routes")
    val routes: List<Route>
)

data class Route(
    @SerializedName("bounds")
    val bounds: Bound,
    /*@SerializedName("legs")
    val legs: List<Legs>*/
)

data class Bound(
    @SerializedName("northeast")
    val northeast: LatLon,
    @SerializedName("southwest")
    val southwest: LatLon,
)

data class LatLon(
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("lng")
    val longitude: Double,
)
