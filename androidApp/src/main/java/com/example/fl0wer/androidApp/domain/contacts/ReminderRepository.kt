package com.example.fl0wer.androidApp.domain.contacts

import java.util.GregorianCalendar

interface ReminderRepository {
    suspend fun birthdayReminder(contact: Contact): Boolean
    suspend fun addBirthdayReminder(contact: Contact, nextBirthday: GregorianCalendar)
    suspend fun removeBirthdayReminder(contact: Contact)
}
