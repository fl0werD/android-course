package com.example.fl0wer.domain.contacts

interface ContactsInteractor {
    suspend fun contacts(forceUpdate: Boolean = false): List<Contact>
    suspend fun contact(lookupKey: String): Contact?
    suspend fun search(nameFilter: String): List<Contact>
    suspend fun contactsWithAddress(ignoreContactId: Int): List<Contact>
}
