package com.example.fl0wer.contacts

import com.example.fl0wer.Contact

interface ContactsRepository {
    fun contacts(): List<Contact>
    suspend fun contact(lookupKey: String): Contact?
    suspend fun loadContacts(): List<Contact>
}
