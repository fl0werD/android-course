package com.example.fl0wer.androidApp.data.contacts.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fl0wer.domain.contacts.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts")
    fun loadLocations(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE id = :id")
    fun loadLocation(id: Int): Flow<ContactEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contactEntity: ContactEntity)
}
