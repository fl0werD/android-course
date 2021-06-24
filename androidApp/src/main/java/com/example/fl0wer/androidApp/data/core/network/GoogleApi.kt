package com.example.fl0wer.androidApp.data.core.network

import com.example.fl0wer.androidApp.data.directions.network.DirectionsResponse
import com.example.fl0wer.androidApp.data.locations.network.GeocodeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleApi {
    @GET("geocode/json")
    suspend fun reverseGeocode(
        @Query("latlng") latLng: String,
    ): GeocodeResponse

    @GET("directions/json")
    suspend fun getRoute(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
    ): DirectionsResponse
}
