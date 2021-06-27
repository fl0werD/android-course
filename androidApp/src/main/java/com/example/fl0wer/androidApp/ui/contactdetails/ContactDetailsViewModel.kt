package com.example.fl0wer.androidApp.ui.contactdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fl0wer.androidApp.data.contacts.ContactMapper.toContact
import com.example.fl0wer.androidApp.data.contacts.ContactMapper.toParcelable
import com.example.fl0wer.androidApp.data.contacts.ContactParcelable
import com.example.fl0wer.androidApp.data.locations.LocationMapper.toParcelable
import com.example.fl0wer.androidApp.ui.contactlist.ContactListState
import com.example.fl0wer.androidApp.ui.core.navigation.Screens
import com.example.fl0wer.androidApp.ui.nullOr
import com.example.fl0wer.domain.contacts.ContactsInteractor
import com.example.fl0wer.domain.contacts.ReminderInteractor
import com.example.fl0wer.domain.core.dispatchers.DispatchersProvider
import com.example.fl0wer.domain.locations.LocationInteractor
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.back
import com.github.terrakok.modo.forward
import com.google.android.gms.maps.model.LatLng
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

@Suppress("SwallowedException")
class ContactDetailsViewModel @AssistedInject constructor(
    private val contactsInteractor: ContactsInteractor,
    private val reminderInteractor: ReminderInteractor,
    private val locationInteractor: LocationInteractor,
    private val dispatchersProvider: DispatchersProvider,
    private val modo: Modo,
    @Assisted contactLookupKey: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ContactDetailsState>(ContactDetailsState.Loading)
    val uiState get() = _uiState.asStateFlow()
    private val vmScope = viewModelScope + CoroutineExceptionHandler { _, e ->
        if (e !is CancellationException) {
            Timber.e(e)
        }
    }

    init {
        loadContact(contactLookupKey)
    }

    fun changeBirthdayReminder() {
        val currentState = uiState.value.nullOr<ContactDetailsState.Idle>() ?: return
        vmScope.launch {
            try {
                val contact = currentState.contact.toContact()
                reminderInteractor.changeBirthdayReminder(contact)
                _uiState.value = currentState.copy(
                    birthdayReminder = reminderInteractor.birthdayReminder(contact)
                )
            } catch (e: IOException) {
                _uiState.value = ContactDetailsState.Failure
            }
        }
    }

    fun addressClicked() {
        uiState.value.nullOr<ContactDetailsState.Idle>() ?: return
        modo.forward(Screens.ContactLocations())
    }

    fun routesClicked() {
        uiState.value.nullOr<ContactListState.Idle>() ?: return
        modo.forward(Screens.ContactsRoute(1, 5))
    }

    fun backPressed() {
        modo.back()
    }

    private fun loadContact(lookupKey: String) {
        vmScope.launch {
            _uiState.value = ContactDetailsState.Loading
            try {
                val result = contactsInteractor.contact(lookupKey)
                if (result != null) {
                    val contact = result.toParcelable()
                    _uiState.value = ContactDetailsState.Idle(
                        contact,
                        reminderInteractor.birthdayReminder(result)
                    )
                    subscribeLocation(contact)
                } else {
                    _uiState.value = ContactDetailsState.Failure
                }
            } catch (e: IOException) {
                _uiState.value = ContactDetailsState.Failure
            }
        }
    }

    private fun subscribeLocation(contact: ContactParcelable) = vmScope.launch {
        locationInteractor.observeLocation(contact.id)
            .map {
                val currentState = uiState.value
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
                if (newState != null) {
                    _uiState.value = newState
                }
            }
    }

    companion object {
        fun provideFactory(
            assistedFactory: ContactDetailsViewModelFactory,
            contactLookupKey: String,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(contactLookupKey) as T
            }
        }
    }
}
