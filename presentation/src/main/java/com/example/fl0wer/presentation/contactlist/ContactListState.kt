package com.example.fl0wer.presentation.contactlist

import com.example.fl0wer.UiState
import com.example.fl0wer.data.ContactParcelable
import kotlinx.parcelize.Parcelize

sealed class ContactListState : UiState {
    @Parcelize
    data class Idle(
        val contacts: List<ContactParcelable>,
    ) : ContactListState()

    @Parcelize
    object Loading : ContactListState()

    @Parcelize
    object Failure : ContactListState()
}
