package com.example.fl0wer

import com.example.fl0wer.presentation.contactdetails.ContactDetailsFragment
import com.example.fl0wer.presentation.contactlist.ContactListFragment
import com.github.terrakok.modo.android.AppScreen
import kotlinx.parcelize.Parcelize

object Screens {
    @Parcelize
    class ContactList : AppScreen("ContactList") {
        override fun create() = ContactListFragment.newInstance()
    }

    @Parcelize
    class ContactDetails(
        private val contactId: String,
    ) : AppScreen("Contact_$contactId") {
        override fun create() = ContactDetailsFragment.newInstance(contactId)
    }
}
