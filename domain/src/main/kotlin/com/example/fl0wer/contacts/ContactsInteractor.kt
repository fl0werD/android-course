package com.example.fl0wer.contacts

import com.example.fl0wer.Contact

interface ContactsInteractor {
    suspend fun contacts(): List<Contact>
    suspend fun contact(lookupKey: String): Contact?
    suspend fun search(nameFilter: String): List<Contact>
}
