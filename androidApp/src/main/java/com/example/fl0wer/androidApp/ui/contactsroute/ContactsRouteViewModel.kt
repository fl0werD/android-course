package com.example.fl0wer.androidApp.ui.contactsroute

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fl0wer.androidApp.data.contacts.ContactMapper.toParcelable
import com.example.fl0wer.androidApp.data.directions.toParcelable
import com.example.fl0wer.androidApp.ui.contactlist.adapter.ContactListItem
import com.example.fl0wer.androidApp.domain.contacts.Contact
import com.example.fl0wer.androidApp.domain.contacts.ContactsInteractor
import com.example.fl0wer.androidApp.domain.core.Result
import com.example.fl0wer.androidApp.domain.directions.DirectionInteractor
import com.example.fl0wer.androidApp.domain.locations.LocationInteractor
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

class ContactsRouteViewModel @Inject constructor(
    private val contactsInteractor: ContactsInteractor,
    private val locationInteractor: LocationInteractor,
    private val directionInteractor: DirectionInteractor,
    private val modo: Modo,
    private val screenParams: ContactsRouteScreenParams,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ContactsRouteState())
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
        val contactItem = uiState.value.contacts.getOrNull(position) ?: return
        if (contactItem !is ContactListItem.Contact) {
            return
        }
        buildRoute(screenParams.startContactId, contactItem.contact.id)
    }

    fun backPressed() {
        modo.back()
    }

    private fun loadContacts() {
        vmScope.launch {
            _uiState.value = uiState.value.copy(loading = true)
            try {
                val contacts = contactsInteractor.contactsWithAddress(screenParams.startContactId)
                _uiState.value = uiState.value.copy(
                    loading = false,
                    contacts = contacts.toListItems(),
                    error = null,
                )
            } catch (e: IOException) {
                Timber.e(e)
                _uiState.value = uiState.value.copy(
                    loading = false,
                    error = e,
                )
            }
        }
    }

    private fun buildRoute(startContactId: Int, endContactId: Int) {
        vmScope.launch {
            _uiState.value = uiState.value.copy(
                loading = true,
                error = null,
            )
            try {
                val start = locationInteractor.observeLocation(startContactId).first()
                val end = locationInteractor.observeLocation(endContactId).first()
                if (start != null && end != null) {
                    when (val route = directionInteractor.route(start.address, end.address)) {
                        is Result.Success -> {
                            _uiState.value = uiState.value.copy(
                                loading = false,
                                route = route.value.toParcelable(),
                                error = null,
                            )
                        }
                        is Result.Failure -> {
                            _uiState.value = uiState.value.copy(
                                loading = false,
                                error = route.throwable,
                            )
                        }
                    }
                }
            } catch (e: IOException) {
                Timber.e(e)
                _uiState.value = uiState.value.copy(
                    loading = false,
                    error = null,
                )
            }
        }
    }

    private fun List<Contact>.toListItems() = map {
        ContactListItem.Contact(it.toParcelable())
    }
}
