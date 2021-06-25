package com.example.fl0wer.androidApp.ui.contactlist.adapter

import android.os.Parcelable
import com.example.fl0wer.androidApp.data.contacts.ContactParcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactListItem(
    val contact: ContactParcelable,
): Parcelable
