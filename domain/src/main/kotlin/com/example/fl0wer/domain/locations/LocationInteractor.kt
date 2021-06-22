package com.example.fl0wer.domain.locations

interface LocationInteractor {
    suspend fun mapClicked(location: Location)
    suspend fun location(contactId: Int): Location?
    suspend fun locations(): List<Location>
}
