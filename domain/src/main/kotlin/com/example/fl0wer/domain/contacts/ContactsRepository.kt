package com.example.fl0wer.domain.contacts

interface ContactsRepository {
    fun contacts(): List<Contact>
    suspend fun contact(lookupKey: String): Contact?
    suspend fun loadContacts(): List<Contact>
}
