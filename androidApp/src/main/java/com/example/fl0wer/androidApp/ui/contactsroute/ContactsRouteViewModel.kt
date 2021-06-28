package com.example.fl0wer.androidApp.ui.contactsroute

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fl0wer.androidApp.data.contacts.ContactMapper.toParcelable
import com.example.fl0wer.androidApp.data.directions.toParcelable
import com.example.fl0wer.androidApp.ui.contactlist.adapter.ContactListItem
import com.example.fl0wer.androidApp.ui.nullOr
import com.example.fl0wer.domain.contacts.Contact
import com.example.fl0wer.domain.contacts.ContactsInteractor
import com.example.fl0wer.domain.core.Result
import com.example.fl0wer.domain.directions.DirectionInteractor
import com.example.fl0wer.domain.locations.LocationInteractor
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.back
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@Suppress("SwallowedException")
class ContactsRouteViewModel @Inject constructor(
    private val contactsInteractor: ContactsInteractor,
    private val locationInteractor: LocationInteractor,
    private val directionInteractor: DirectionInteractor,
    private val modo: Modo,
    private val screenParams: ContactsRouteScreenParams,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ContactsRouteState>(ContactsRouteState.Loading)
    val uiState get() = _uiState.asStateFlow()
    private val vmScope = viewModelScope + CoroutineExceptionHandler { _, e ->
        if (e !is CancellationException) {
            Timber.e(e)
        }
    }

    init {
        loadContacts()
    }

    fun contactClicked(position: Int) {
        val currentState = uiState.value.nullOr<ContactsRouteState.Idle>() ?: return
        val endContactId = (currentState.contacts[position] as ContactListItem.Contact).contact.id
        buildRoute(screenParams.startContactId, endContactId)
    }

    fun backPressed() {
        modo.back()
    }

    private fun buildRoute(startContactId: Int, endContactId: Int) {
        vmScope.launch {
            // _uiState.value = ContactsRouteState.Loading
            try {
                val start = locationInteractor.observeLocation(startContactId).first()
                val end = locationInteractor.observeLocation(endContactId).first()
                if (start != null && end != null) {
                    when (val route = directionInteractor.route(start.address, end.address)) {
                        is Result.Success -> {
                            val currentState = uiState.value
                            val contacts = if (currentState is ContactsRouteState.Idle) {
                                currentState.contacts
                            } else {
                                emptyList()
                            }
                            _uiState.value = ContactsRouteState.Idle(
                                route = route.value.toParcelable(),
                                contacts = contacts,
                            )
                        }
                        is Result.Failure -> {
                            _uiState.value = ContactsRouteState.RouteNotFound
                        }
                    }
                } else {
                    _uiState.value = ContactsRouteState.EmptyAddress
                }
            } catch (e: IOException) {
                Timber.e(e)
            }
        }
    }

    private fun loadContacts() {
        vmScope.launch {
            _uiState.value = ContactsRouteState.Loading
            try {
                val contacts = contactsInteractor.contactsWithAddress(screenParams.startContactId)
                _uiState.value = ContactsRouteState.Idle(
                    contacts = contacts.toListItems()
                )
            } catch (e: IOException) {
                // _uiState.value = ContactsRouteState.Failure
            }
        }
    }

    private fun List<Contact>.toListItems() = map {
        ContactListItem.Contact(it.toParcelable())
    }
}
