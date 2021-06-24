package com.example.fl0wer.domain.directions

interface DirectionRepository {
    suspend fun getRoute(origin: String, destination: String): Route?
}
