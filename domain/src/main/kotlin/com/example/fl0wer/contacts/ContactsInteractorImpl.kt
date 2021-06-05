package com.example.fl0wer.contacts

import com.example.fl0wer.Contact

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
}
