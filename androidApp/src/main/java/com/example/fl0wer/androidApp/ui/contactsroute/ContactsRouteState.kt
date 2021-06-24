package com.example.fl0wer.androidApp.ui.contactsroute

import com.example.fl0wer.androidApp.data.directions.RouteParcelable
import com.example.fl0wer.androidApp.ui.UiState
import kotlinx.parcelize.Parcelize

sealed class ContactsRouteState : UiState {
    @Parcelize
    object Loading : ContactsRouteState()

    @Parcelize
    data class Idle(
        val route: RouteParcelable,
    ) : ContactsRouteState()

    @Parcelize
    object EmptyAddress : ContactsRouteState()

    @Parcelize
    object RouteNotFound : ContactsRouteState()
}
