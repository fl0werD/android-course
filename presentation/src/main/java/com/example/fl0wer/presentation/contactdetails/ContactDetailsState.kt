package com.example.fl0wer.presentation.contactdetails

import com.example.fl0wer.UiState
import com.example.fl0wer.data.ContactParcelable
import kotlinx.parcelize.Parcelize

sealed class ContactDetailsState : UiState {
    @Parcelize
    data class Idle(
        val contact: ContactParcelable,
        val birthdayReminder: Boolean = false,
    ) : ContactDetailsState()

    @Parcelize
    object Loading : ContactDetailsState()

    @Parcelize
    object Failure : ContactDetailsState()
}
