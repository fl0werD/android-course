package com.example.fl0wer.androidApp.data.directions.network

import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsApi {
    @GET("json?")
    suspend fun getRoute(
        @Query("origin")
        origin: String,
        @Query("destination")
        destination: String,
    ): DirectionResponse
}
