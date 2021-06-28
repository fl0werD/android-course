package com.example.fl0wer.androidApp.ui.contactlocation

import com.example.fl0wer.androidApp.data.locations.LocationParcelable
import com.example.fl0wer.androidApp.ui.UiState
import kotlinx.parcelize.Parcelize

sealed class ContactLocationState : UiState {
    @Parcelize
    object Loading : ContactLocationState()

    @Parcelize
    data class Idle(
        val location: LocationParcelable? = null,
    ) : ContactLocationState()
}
