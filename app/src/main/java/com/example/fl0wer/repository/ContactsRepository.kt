package com.example.fl0wer.repository

import android.content.Context
import com.example.fl0wer.Contact
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface ContactsRepository {
    fun birthdayNotice(contact: Contact): Boolean
    fun getContacts(): Observable<List<Contact>>
    fun getContactById(lookupKey: String): Single<Contact>
    fun getSearchedContacts(nameFilter: String): Observable<List<Contact>>

    companion object {
        private var INSTANCE: ContactsRepository? = null

        fun get(context: Context): ContactsRepository =
            INSTANCE ?: ContactsRepositoryImpl(context)
                .also {
                    INSTANCE = it
                }
    }
}
