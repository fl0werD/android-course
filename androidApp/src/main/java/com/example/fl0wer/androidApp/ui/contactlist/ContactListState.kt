package com.example.fl0wer.androidApp.ui.contactlist

import com.example.fl0wer.androidApp.ui.UiState
import com.example.fl0wer.androidApp.data.contacts.ContactParcelable
import com.example.fl0wer.androidApp.ui.contactlist.adapter.ContactListItem
import kotlinx.parcelize.Parcelize

sealed class ContactListState : UiState {
    @Parcelize
    data class Idle(
        val contacts: List<ContactListItem>,
    ) : ContactListState()

    @Parcelize
    object Loading : ContactListState()

    @Parcelize
    object Failure : ContactListState()
}
