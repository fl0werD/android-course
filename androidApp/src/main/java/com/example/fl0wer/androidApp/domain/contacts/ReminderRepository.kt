package com.example.fl0wer.androidApp.domain.contacts

import java.util.GregorianCalendar

interface ReminderRepository {
    fun birthdayReminder(contact: Contact): Boolean
    fun addBirthdayReminder(contact: Contact, nextBirthday: GregorianCalendar)
    fun removeBirthdayReminder(contact: Contact)
}
