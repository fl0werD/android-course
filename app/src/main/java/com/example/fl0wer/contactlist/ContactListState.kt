package com.example.fl0wer.contactlist

import com.example.fl0wer.Contact

sealed class ContactListState {
    data class Idle(
        val contact: Contact
    ) : ContactListState()

    object Loading : ContactListState()
    object Empty : ContactListState()
}
