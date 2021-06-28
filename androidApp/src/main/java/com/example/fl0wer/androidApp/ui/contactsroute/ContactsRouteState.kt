package com.example.fl0wer.androidApp.ui.contactsroute

import com.example.fl0wer.androidApp.data.directions.RouteParcelable
import com.example.fl0wer.androidApp.ui.UiState
import com.example.fl0wer.androidApp.ui.contactlist.adapter.ContactListItem
import kotlinx.parcelize.Parcelize

sealed class ContactsRouteState : UiState {
    @Parcelize
    object Loading : ContactsRouteState()

    @Parcelize
    data class Idle(
        val route: RouteParcelable? = null,
        val contacts: List<ContactListItem>,
    ) : ContactsRouteState()

    @Parcelize
    object EmptyAddress : ContactsRouteState()

    @Parcelize
    object RouteNotFound : ContactsRouteState()
}
