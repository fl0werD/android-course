package com.example.fl0wer.contactdetails

import com.example.fl0wer.Contact
import com.example.fl0wer.UiState
import kotlinx.parcelize.Parcelize

sealed class ContactDetailsState : UiState {
    @Parcelize
    data class Idle(
        val contact: Contact,
        val birthdayNotice: Boolean,
    ) : ContactDetailsState()

    @Parcelize
    object Loading : ContactDetailsState()

    @Parcelize
    object Failure : ContactDetailsState()
}
