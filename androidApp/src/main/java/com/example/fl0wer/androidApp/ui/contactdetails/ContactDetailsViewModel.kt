package com.example.fl0wer.androidApp.ui.contactdetails

import com.example.fl0wer.androidApp.data.contacts.ContactMapper.toContact
import com.example.fl0wer.androidApp.data.contacts.ContactMapper.toParcelable
import com.example.fl0wer.androidApp.data.contacts.ContactParcelable
import com.example.fl0wer.androidApp.data.locations.LocationMapper.toParcelable
import com.example.fl0wer.androidApp.domain.contacts.ContactsInteractor
import com.example.fl0wer.androidApp.domain.contacts.ReminderInteractor
import com.example.fl0wer.androidApp.domain.locations.LocationInteractor
import com.example.fl0wer.androidApp.ui.core.BaseViewModel
import com.example.fl0wer.androidApp.ui.core.dispatchers.DispatchersProvider
import com.example.fl0wer.androidApp.ui.core.navigation.Screens
import com.example.fl0wer.androidApp.ui.nullOr
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.forward
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

class ContactDetailsViewModel @Inject constructor(
    private val modo: Modo,
    private val contactsInteractor: ContactsInteractor,
    private val reminderInteractor: ReminderInteractor,
    private val locationInteractor: LocationInteractor,
    private val dispatchersProvider: DispatchersProvider,
    screenParams: ContactDetailsScreenParams,
) : BaseViewModel<ContactDetailsState>(
    initState = ContactDetailsState.Loading,
    modo,
) {
    init {
        loadContact(screenParams.contactLookupKey)
    }

    fun changeBirthdayReminder() {
        val currentState = uiState.nullOr<ContactDetailsState.Idle>() ?: return
        vmScope.launch {
            try {
                val contact = currentState.contact.toContact()
                reminderInteractor.changeBirthdayReminder(contact)
                updateState(
                    currentState.copy(
                        birthdayReminder = reminderInteractor.birthdayReminder(contact)
                    )
                )
            } catch (e: IOException) {
                Timber.e(e)
                updateState(ContactDetailsState.Failure)
            }
        }
    }

    fun addressClicked() {
        val currentState = uiState.nullOr<ContactDetailsState.Idle>() ?: return
        modo.forward(Screens.ContactLocation(currentState.contact.id))
    }

    fun routesClicked() {
        val currentState = uiState.nullOr<ContactDetailsState.Idle>() ?: return
        modo.forward(Screens.ContactsRoute(currentState.contact.id))
    }

    private fun loadContact(lookupKey: String) {
        vmScope.launch {
            updateState(ContactDetailsState.Loading)
            try {
                val result = contactsInteractor.contact(lookupKey)
                if (result != null) {
                    val contact = result.toParcelable()
                    updateState(
                        ContactDetailsState.Idle(
                            contact,
                            reminderInteractor.birthdayReminder(result)
                        )
                    )
                    subscribeLocation(contact)
                } else {
                    updateState(ContactDetailsState.Failure)
                }
            } catch (e: IOException) {
                Timber.e(e)
                updateState(ContactDetailsState.Failure)
            }
        }
    }

    private fun subscribeLocation(contact: ContactParcelable) = vmScope.launch {
        locationInteractor.observeLocation(contact.id)
            .map {
                val currentState = uiState
                if (currentState is ContactDetailsState.Idle) {
                    currentState.copy(
                        location = it?.toParcelable()
                    )
                } else {
                    null
                }
            }
            .flowOn(dispatchersProvider.io)
            .collect { newState ->
                if (newState == null) {
                    return@collect
                }
                updateState(newState)
            }
    }
}
