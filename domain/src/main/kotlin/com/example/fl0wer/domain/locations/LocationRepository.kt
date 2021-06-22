package com.example.fl0wer.domain.locations

interface LocationRepository {
    suspend fun location(contactId: Int): Location?
    suspend fun locations(): List<Location>
    suspend fun save(location: Location)
}
