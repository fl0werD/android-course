package com.example.fl0wer.androidApp.ui.contactdetails

import com.example.fl0wer.androidApp.ui.UiState
import com.example.fl0wer.androidApp.data.contacts.ContactParcelable
import com.example.fl0wer.androidApp.data.locations.LocationParcelable
import com.example.fl0wer.domain.locations.Location
import kotlinx.parcelize.Parcelize

sealed class ContactDetailsState : UiState {
    @Parcelize
    data class Idle(
        val contact: ContactParcelable,
        val birthdayReminder: Boolean = false,
        val location: LocationParcelable? = null,
    ) : ContactDetailsState()

    @Parcelize
    object Loading : ContactDetailsState()

    @Parcelize
    object Failure : ContactDetailsState()
}
