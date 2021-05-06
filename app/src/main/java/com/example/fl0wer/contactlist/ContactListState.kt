package com.example.fl0wer.contactlist

import com.example.fl0wer.Contact
import com.example.fl0wer.UiState
import kotlinx.parcelize.Parcelize

sealed class ContactListState : UiState {
    @Parcelize
    data class Idle(
        val contacts: List<Contact>,
    ) : ContactListState()

    @Parcelize
    object Loading : ContactListState()

    @Parcelize
    object Failure : ContactListState()
}
