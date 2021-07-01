package com.example.fl0wer.androidApp.domain.directions

import com.example.fl0wer.androidApp.domain.core.Result

interface DirectionRepository {
    suspend fun getRoute(origin: String, destination: String): Result<Route>
}
