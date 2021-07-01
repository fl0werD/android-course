package com.example.fl0wer.androidApp.domain.directions

import com.example.fl0wer.androidApp.domain.core.Result

interface DirectionInteractor {
    suspend fun route(origin: String, destination: String): Result<Route>
}
