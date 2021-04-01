package com.example.fl0wer

object Contacts {
    val contacts = mutableListOf<Contact>()

    fun getUserById(lookupKey: String): Contact? {
        return contacts.find {
            it.lookupKey == lookupKey
        }
    }

    fun addContact(contact: Contact) {
        contacts.add(contact)
    }

    fun updateContactDetails(lookupKey: String, newContact: Contact) {
        contacts.map {
            if (it.lookupKey == lookupKey) {
                newContact
            } else {
                it
            }
        }
    }

    fun clearContacts() {
        contacts.clear()
    }
}
