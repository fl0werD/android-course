package com.example.fl0wer.domain.directions

import com.example.fl0wer.domain.core.Result

interface DirectionRepository {
    suspend fun getRoute(origin: String, destination: String): Result<Route>
}
