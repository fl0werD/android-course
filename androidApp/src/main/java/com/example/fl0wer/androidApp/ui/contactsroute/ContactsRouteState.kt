package com.example.fl0wer.androidApp.ui.contactsroute

import com.example.fl0wer.androidApp.data.directions.RouteParcelable
import com.example.fl0wer.androidApp.ui.UiState
import com.example.fl0wer.androidApp.ui.contactlist.adapter.ContactListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactsRouteState(
    val loading: Boolean = false,
    val route: RouteParcelable? = null,
    val contacts: List<ContactListItem> = mutableListOf(),
    val error: Throwable? = null,
) : UiState
