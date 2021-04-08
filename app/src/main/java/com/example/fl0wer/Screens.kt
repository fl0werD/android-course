package com.example.fl0wer

import com.example.fl0wer.contactdetails.ContactDetailsFragment
import com.example.fl0wer.contactlist.ContactListFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {
    fun contactList() = FragmentScreen {
        ContactListFragment.newInstance()
    }

    fun contactDetails(contactId: String) = FragmentScreen {
        ContactDetailsFragment.newInstance(contactId)
    }
}
