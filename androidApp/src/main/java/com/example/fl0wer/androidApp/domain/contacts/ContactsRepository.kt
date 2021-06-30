package com.example.fl0wer.androidApp.domain.contacts

interface ContactsRepository {
    suspend fun contacts(forceUpdate: Boolean = false): List<Contact>
    suspend fun contact(lookupKey: String): Contact?
}
