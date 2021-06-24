package com.example.fl0wer.domain.directions

interface DirectionInteractor {
    suspend fun route(origin: String, destination: String): Route?
}
