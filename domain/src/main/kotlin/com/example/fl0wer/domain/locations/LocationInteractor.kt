package com.example.fl0wer.domain.locations

import kotlinx.coroutines.flow.Flow

interface LocationInteractor {
    fun observeLocation(contactId: Int): Flow<Location?>
    suspend fun mapClicked(contactId: Int, latitude: Double, longitude: Double)
    suspend fun locations(): List<Location>
}
