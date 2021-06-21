package com.example.fl0wer.androidApp.ui.contactdetails

import com.example.fl0wer.androidApp.ui.UiState
import com.example.fl0wer.androidApp.data.contacts.ContactParcelable
import kotlinx.parcelize.Parcelize

sealed class ContactDetailsState : UiState {
    @Parcelize
    data class Idle(
        val contact: ContactParcelable,
        val birthdayReminder: Boolean = false,
        val latitude: Double,
        val longitude: Double,
    ) : ContactDetailsState()

    @Parcelize
    object Loading : ContactDetailsState()

    @Parcelize
    object Failure : ContactDetailsState()
}
