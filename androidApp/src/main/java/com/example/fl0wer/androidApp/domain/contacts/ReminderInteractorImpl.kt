package com.example.fl0wer.androidApp.domain.contacts

import java.util.GregorianCalendar

class ReminderInteractorImpl(
    private val reminderRepository: ReminderRepository,
    private val date: GregorianCalendar,
) : ReminderInteractor {
    override suspend fun birthdayReminder(contact: Contact): Boolean {
        return reminderRepository.birthdayReminder(contact)
    }

    override suspend fun addBirthdayReminder(contact: Contact) {
        val nextBirthday = calculateNextBirthdayDate(contact.birthdayMonth, contact.birthdayDayOfMonth)
        reminderRepository.addBirthdayReminder(contact, nextBirthday)
    }

    override suspend fun removeBirthdayReminder(contact: Contact) {
        reminderRepository.removeBirthdayReminder(contact)
    }

    override suspend fun changeBirthdayReminder(contact: Contact) {
        if (reminderRepository.birthdayReminder(contact)) {
            reminderRepository.removeBirthdayReminder(contact)
        } else {
            val nextBirthday = calculateNextBirthdayDate(contact.birthdayMonth, contact.birthdayDayOfMonth)
            reminderRepository.addBirthdayReminder(contact, nextBirthday)
        }
    }

    @Suppress("MagicNumber")
    private fun calculateNextBirthdayDate(month: Int, dayOfMonth: Int) =
        GregorianCalendar().apply {
            val currentYear = date.get(GregorianCalendar.YEAR)
            set(GregorianCalendar.YEAR, currentYear)
            set(GregorianCalendar.MONTH, month)
            set(GregorianCalendar.DAY_OF_MONTH, dayOfMonth)
            if (timeInMillis < date.timeInMillis) {
                add(GregorianCalendar.YEAR, 1)
            }
            if (month == GregorianCalendar.FEBRUARY && dayOfMonth == 29) {
                while (!isLeapYear(get(GregorianCalendar.YEAR))) {
                    add(GregorianCalendar.YEAR, 1)
                }
                set(GregorianCalendar.MONTH, GregorianCalendar.FEBRUARY)
                set(GregorianCalendar.DAY_OF_MONTH, 29)
            }
        }
}
