package com.example.fl0wer.domain.contacts

interface ContactsInteractor {
    suspend fun contacts(): List<Contact>
    suspend fun contact(lookupKey: String): Contact?
    suspend fun search(nameFilter: String): List<Contact>
}
