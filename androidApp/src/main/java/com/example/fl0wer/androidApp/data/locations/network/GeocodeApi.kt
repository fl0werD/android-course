package com.example.fl0wer.androidApp.data.locations.network

import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodeApi {
    @GET("json?")
    suspend fun reverseGeocode(
        @Query("latlng")
        latLng: String,
    ): GeocodeResponse
}
