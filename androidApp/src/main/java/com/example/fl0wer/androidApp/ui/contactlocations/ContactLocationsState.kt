package com.example.fl0wer.androidApp.ui.contactlocations

import com.example.fl0wer.androidApp.ui.UiState
import kotlinx.parcelize.Parcelize

sealed class ContactLocationsState : UiState {
    @Parcelize
    data class Idle(
        val bool: Boolean,
    ) : ContactLocationsState()

    @Parcelize
    object Loading : ContactLocationsState()

    @Parcelize
    object Failure : ContactLocationsState()
}
