package com.example.fl0wer.androidApp.data.contacts.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fl0wer.BuildConfig

@Database(
    entities = [
        ContactEntity::class
    ],
    version = BuildConfig.DB_VERSION,
    exportSchema = false,
)
abstract class ContactDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}
