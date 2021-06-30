package com.example.fl0wer.androidApp.ui.contactsroute

import com.example.fl0wer.androidApp.data.contacts.ContactMapper.toParcelable
import com.example.fl0wer.androidApp.data.directions.toParcelable
import com.example.fl0wer.androidApp.domain.contacts.Contact
import com.example.fl0wer.androidApp.domain.contacts.ContactsInteractor
import com.example.fl0wer.androidApp.domain.core.Result
import com.example.fl0wer.androidApp.domain.directions.DirectionInteractor
import com.example.fl0wer.androidApp.domain.locations.LocationInteractor
import com.example.fl0wer.androidApp.ui.contactlist.adapter.ContactListItem
import com.example.fl0wer.androidApp.ui.core.BaseViewModel
import com.github.terrakok.modo.Modo
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

class ContactsRouteViewModel @Inject constructor(
    modo: Modo,
    private val contactsInteractor: ContactsInteractor,
    private val locationInteractor: LocationInteractor,
    private val directionInteractor: DirectionInteractor,
    private val screenParams: ContactsRouteScreenParams,
) : BaseViewModel<ContactsRouteState>(
    initState = ContactsRouteState(),
    modo,
) {
    init {
        loadContacts()
    }

    fun contactClicked(position: Int) {
        val contactItem = uiState.contacts.getOrNull(position) ?: return
        if (contactItem !is ContactListItem.Contact) {
            return
        }
        buildRoute(screenParams.startContactId, contactItem.contact.id)
    }

    private fun loadContacts() {
        vmScope.launch {
            updateState { uiState.copy(loading = true) }
            try {
                val contacts = contactsInteractor.contactsWithAddress(screenParams.startContactId)
                updateState {
                    uiState.copy(
                        loading = false,
                        contacts = contacts.toListItems(),
                        error = null,
                    )
                }
            } catch (e: IOException) {
                Timber.e(e)
                updateState {
                    uiState.copy(
                        loading = false,
                        error = e,
                    )
                }
            }
        }
    }

    private fun buildRoute(startContactId: Int, endContactId: Int) {
        vmScope.launch {
            updateState {
                uiState.copy(
                    loading = true,
                    error = null,
                )
            }
            try {
                val start = locationInteractor.observeLocation(startContactId).first()
                val end = locationInteractor.observeLocation(endContactId).first()
                if (start != null && end != null) {
                    when (val route = directionInteractor.route(start.address, end.address)) {
                        is Result.Success -> {
                            updateState {
                                uiState.copy(
                                    loading = false,
                                    route = route.value.toParcelable(),
                                    error = null,
                                )
                            }
                        }
                        is Result.Failure -> {
                            updateState {
                                uiState.copy(
                                    loading = false,
                                    error = route.throwable,
                                )
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                Timber.e(e)
                updateState {
                    uiState.copy(
                        loading = false,
                        error = null,
                    )
                }
            }
        }
    }

    private fun List<Contact>.toListItems() = map {
        ContactListItem.Contact(it.toParcelable())
    }
}
