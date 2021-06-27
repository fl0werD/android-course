package com.example.fl0wer.androidApp.ui.contactlist.adapter

import android.os.Parcelable
import com.example.fl0wer.androidApp.data.contacts.ContactParcelable
import kotlinx.parcelize.Parcelize

sealed class ContactListItem : Parcelable {
    @Parcelize
    data class Contact(
        val contact: ContactParcelable,
    ): ContactListItem()

    @Parcelize
    data class Footer(
        val count: Int,
    ) : ContactListItem()
}
