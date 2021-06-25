package com.example.fl0wer.domain.directions

import com.example.fl0wer.domain.core.Result

interface DirectionInteractor {
    suspend fun route(origin: String, destination: String): Result<Route>
}
