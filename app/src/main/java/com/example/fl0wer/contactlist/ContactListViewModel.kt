package com.example.fl0wer.contactlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fl0wer.CiceroneHolder
import com.example.fl0wer.Screens
import com.example.fl0wer.dispatchers.DispatchersProvider
import com.example.fl0wer.nullOr
import com.example.fl0wer.repository.ContactsRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

class ContactListViewModel(
    private val contactsRepository: ContactsRepository,
    private val dispatchersProvider: DispatchersProvider,
) : ViewModel() {
    private val disposables = CompositeDisposable()
    private val stateLiveData = MutableLiveData<ContactListState>()
    private val state: ContactListState
        get() = stateLiveData.value ?: throw IllegalStateException("state value should not be null")

    init {
        getContacts()
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    fun getScreenState(): LiveData<ContactListState> {
        return stateLiveData
    }

    fun contactClicked(position: Int) {
        val currentState = state.nullOr<ContactListState.Idle>() ?: return
        val contact = currentState.contacts[position]
        CiceroneHolder.router.navigateTo(Screens.contactDetails(contact.lookupKey))
    }

    fun searchTextChanged(nameFilter: String) {
        state.nullOr<ContactListState.Idle>() ?: return
        val contacts = contactsRepository.getSearchedContacts(nameFilter.trim())
            .subscribeOn(dispatchersProvider.io)
            .observeOn(dispatchersProvider.main)
            .doOnSubscribe {
                stateLiveData.value = ContactListState.Loading
            }
            .subscribe(
                { contacts -> stateLiveData.value = ContactListState.Idle(contacts) },
                { stateLiveData.value = ContactListState.Empty }
            )
        disposables.add(contacts)
    }

    private fun getContacts() {
        val contacts = contactsRepository.getContacts()
            .subscribeOn(dispatchersProvider.io)
            .observeOn(dispatchersProvider.main)
            .doOnSubscribe {
                stateLiveData.value = ContactListState.Loading
            }
            .subscribe(
                { contacts -> stateLiveData.value = ContactListState.Idle(contacts) },
                { stateLiveData.value = ContactListState.Empty }
            )
        disposables.add(contacts)
    }
}
