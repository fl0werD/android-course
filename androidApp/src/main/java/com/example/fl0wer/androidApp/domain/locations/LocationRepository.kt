package com.example.fl0wer.androidApp.domain.locations

import kotlinx.coroutines.flow.Flow
import com.example.fl0wer.androidApp.domain.core.Result

interface LocationRepository {
    fun observeLocation(contactId: Int): Flow<Location?>
    suspend fun locations(): List<Location>
    suspend fun save(location: Location)
    suspend fun reverseGeocode(latitude: Double, longitude: Double): Result<String>
}
