package com.example.fl0wer.contacts

import com.example.fl0wer.Contact
import java.util.Calendar
import java.util.GregorianCalendar

class ContactsInteractorImpl(
    private val contactsRepository: ContactsRepository,
) : ContactsInteractor {
    override suspend fun contacts(): List<Contact> {
        val contacts = contactsRepository.contacts()
        return if (contacts.isEmpty()) {
            contactsRepository.loadContacts()
        } else {
            contacts
        }
    }

    override suspend fun contact(lookupKey: String): Contact? {
        return contactsRepository.contact(lookupKey)
    }

    override suspend fun search(nameFilter: String): List<Contact> {
        return if (nameFilter.isNotEmpty()) {
            contactsRepository.contacts().filter {
                it.name.contains(nameFilter, true)
            }
        } else {
            contactsRepository.contacts()
        }
    }

    override suspend fun birthdayNotice(contact: Contact): Boolean {
        return contactsRepository.getBirthdayReminder(contact)
    }

    override suspend fun birthdayReminder(contact: Contact, enabled: Boolean) {
        if (enabled) {
            val nextBirthday = GregorianCalendar().apply {
                val currentDate = GregorianCalendar()
                val currentYear = currentDate.get(Calendar.YEAR)
                timeInMillis = contact.birthdayTimestamp
                set(Calendar.YEAR, currentYear)
                if (timeInMillis < currentDate.timeInMillis) {
                    add(Calendar.YEAR, currentYear + 1)
                }
                if (get(Calendar.MONTH) == Calendar.FEBRUARY &&
                    get(Calendar.DAY_OF_MONTH) == 29 &&
                    !isLeapYear(currentYear)
                ) {
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }
            contactsRepository.addBirthdayReminder(contact, nextBirthday.timeInMillis)
        } else {
            contactsRepository.removeBirthdayReminder(contact)
        }
    }
}
