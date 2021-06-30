package com.example.fl0wer.androidApp.domain.contacts

import com.example.fl0wer.androidApp.domain.locations.LocationRepository
import com.example.fl0wer.androidApp.ui.core.dispatchers.DispatchersProvider
import kotlinx.coroutines.withContext

class ContactsInteractorImpl(
    private val contactsRepository: ContactsRepository,
    private val locationRepository: LocationRepository,
    private val dispatchersProvider: DispatchersProvider,
) : ContactsInteractor {
    override suspend fun contacts(forceUpdate: Boolean) =
        withContext(dispatchersProvider.io) {
            contactsRepository.contacts(forceUpdate)
        }

    override suspend fun contact(lookupKey: String) =
        withContext(dispatchersProvider.io) {
            contactsRepository.contact(lookupKey)
        }

    override suspend fun search(nameFilter: String) =
        withContext(dispatchersProvider.default) {
            if (nameFilter.isNotEmpty()) {
                contactsRepository.contacts().filter {
                    it.name.contains(nameFilter, true)
                }
            } else {
                contactsRepository.contacts()
            }
        }

    override suspend fun contactsWithAddress(ignoreContactId: Int) =
        withContext(dispatchersProvider.default) {
            val withAddressList = mutableListOf<Contact>()
            contactsRepository.contacts().forEach { contact ->
                locationRepository.locations().forEach { location ->
                    if (contact.id == location.id &&
                        contact.id != ignoreContactId &&
                        location.address.isNotEmpty()
                    ) {
                        withAddressList.add(contact)
                    }
                }
            }
            withAddressList
        }
}
