package com.example.fl0wer.androidApp.domain.contacts

import com.example.fl0wer.androidApp.ui.core.dispatchers.DispatchersProvider
import java.util.GregorianCalendar
import kotlinx.coroutines.withContext

class ReminderInteractorImpl(
    private val reminderRepository: ReminderRepository,
    private val date: GregorianCalendar,
    private val dispatchersProvider: DispatchersProvider,
) : ReminderInteractor {
    override suspend fun birthdayReminder(contact: Contact) =
        withContext(dispatchersProvider.io) {
            reminderRepository.birthdayReminder(contact)
        }

    override suspend fun addBirthdayReminder(contact: Contact) =
        withContext(dispatchersProvider.io) {
            val nextBirthday = calculateNextBirthdayDate(
                contact.birthdayMonth,
                contact.birthdayDayOfMonth,
            )
            reminderRepository.addBirthdayReminder(contact, nextBirthday)
        }

    override suspend fun removeBirthdayReminder(contact: Contact) =
        withContext(dispatchersProvider.io) {
            reminderRepository.removeBirthdayReminder(contact)
        }

    override suspend fun changeBirthdayReminder(contact: Contact) =
        withContext(dispatchersProvider.io) {
            if (reminderRepository.birthdayReminder(contact)) {
                reminderRepository.removeBirthdayReminder(contact)
            } else {
                val nextBirthday =
                    calculateNextBirthdayDate(contact.birthdayMonth, contact.birthdayDayOfMonth)
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
