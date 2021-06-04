package com.example.fl0wer.contacts

import com.example.fl0wer.Contact

interface ReminderInteractor {
    suspend fun birthdayReminder(contact: Contact): Boolean
    suspend fun addBirthdayReminder(contact: Contact)
    suspend fun removeBirthdayReminder(contact: Contact)
    suspend fun changeBirthdayReminder(contact: Contact)
}
