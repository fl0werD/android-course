package com.example.fl0wer.androidApp.domain.contacts

interface ReminderInteractor {
    suspend fun birthdayReminder(contact: Contact): Boolean
    suspend fun addBirthdayReminder(contact: Contact)
    suspend fun removeBirthdayReminder(contact: Contact)
    suspend fun changeBirthdayReminder(contact: Contact)
}
