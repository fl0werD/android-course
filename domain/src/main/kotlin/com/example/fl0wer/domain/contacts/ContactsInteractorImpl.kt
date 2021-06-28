package com.example.fl0wer.domain.contacts

import com.example.fl0wer.domain.locations.LocationRepository

class ContactsInteractorImpl(
    private val contactsRepository: ContactsRepository,
    private val locationRepository: LocationRepository,
) : ContactsInteractor {
    override suspend fun contacts(forceUpdate: Boolean): List<Contact> {
        val contacts = contactsRepository.contacts()
        return if (contacts.isEmpty() || forceUpdate) {
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

    override suspend fun contactsWithAddress(ignoreContactId: Int): List<Contact> {
        val withAddressList = mutableListOf<Contact>()
        contactsRepository.contacts().forEach { contact ->
            locationRepository.locations().forEach { location ->
                if (contact.id == location.id &&
                    contact.id != ignoreContactId &&
                    location.address.isNotEmpty()
                ) {
                    withAddressList.add(contact)
                }
            }
        }
        return withAddressList
    }
}
