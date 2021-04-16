package com.example.fl0wer.contactdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fl0wer.CiceroneHolder
import com.example.fl0wer.dispatchers.DispatchersProvider
import com.example.fl0wer.nullOr
import com.example.fl0wer.repository.ContactsRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

class ContactDetailsViewModel(
    private val contactsRepository: ContactsRepository,
    private val dispatchersProvider: DispatchersProvider,
    param: String,
) : ViewModel() {
    private val disposables = CompositeDisposable()
    private val stateLiveData = MutableLiveData<ContactDetailsState>()
    private val state: ContactDetailsState
        get() = stateLiveData.value ?: throw IllegalStateException("state value should not be null")

    init {
        getContactById(param)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    fun getScreenState(): LiveData<ContactDetailsState> {
        return stateLiveData
    }

    fun changeBirthdayNotice() {
        val currentState = state.nullOr<ContactDetailsState.Idle>() ?: return
        val birthdayNotice = contactsRepository.birthdayNotice(currentState.contact)
        stateLiveData.value = currentState.copy(
            birthdayNotice = birthdayNotice,
        )
    }

    fun backPressed() {
        CiceroneHolder.router.exit()
    }

    private fun getContactById(lookupKey: String) {
        val contact = contactsRepository.getContactById(lookupKey)
            .subscribeOn(dispatchersProvider.io)
            .observeOn(dispatchersProvider.main)
            .doOnSubscribe {
                stateLiveData.value = ContactDetailsState.Loading
            }
            .subscribe(
                { contact ->
                    stateLiveData.value = ContactDetailsState.Idle(
                        contact,
                        false
                    )
                },
                { stateLiveData.value = ContactDetailsState.Error }
            )
        disposables.add(contact)
    }
}
