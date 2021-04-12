package com.example.fl0wer.contactlist

import com.example.fl0wer.Contact
import com.example.fl0wer.ScreenState

sealed class ContactListState : ScreenState {
    data class Idle(
        val contacts: List<Contact>,
    ) : ContactListState()

    object Loading : ContactListState()
    object Empty : ContactListState()
}
