package com.example.fl0wer.domain.locations

interface LocationInteractor {
    suspend fun mapClicked(contactId: Int, latitude: Double, longitude: Double)
    suspend fun location(contactId: Int): Location?
    suspend fun locations(): List<Location>
}
