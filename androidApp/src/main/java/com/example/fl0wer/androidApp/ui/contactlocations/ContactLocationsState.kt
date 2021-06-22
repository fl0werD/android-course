package com.example.fl0wer.androidApp.ui.contactlocations

import com.example.fl0wer.androidApp.data.locations.LocationParcelable
import com.example.fl0wer.androidApp.ui.UiState
import kotlinx.parcelize.Parcelize

sealed class ContactLocationsState : UiState {
    @Parcelize
    object Loading : ContactLocationsState()

    @Parcelize
    data class Idle(
        val locations: List<LocationParcelable>,
    ) : ContactLocationsState()
}
