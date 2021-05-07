package com.example.fl0wer.contacts

import com.example.fl0wer.Contact

interface ContactsRepository {
    fun contacts(): List<Contact>
    suspend fun contact(lookupKey: String): Contact?
    suspend fun loadContacts(): List<Contact>
    suspend fun getBirthdayReminder(contact: Contact): Boolean
    suspend fun addBirthdayReminder(contact: Contact, nextBirthday: Long)
    suspend fun removeBirthdayReminder(contact: Contact)
}
