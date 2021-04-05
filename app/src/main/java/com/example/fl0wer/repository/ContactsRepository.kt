package com.example.fl0wer.repository

import android.content.Context
import com.example.fl0wer.Contact
import com.example.fl0wer.dispatchers.DispatchersProviderImpl

interface ContactsRepository {
    fun birthdayNotice(contact: Contact): Boolean
    suspend fun getFirstContact(): Contact?
    suspend fun getContactById(lookupKey: String): Contact?

    companion object {
        private var INSTANCE: ContactsRepository? = null

        fun get(context: Context): ContactsRepository =
            INSTANCE ?: ContactsRepositoryImpl(context, DispatchersProviderImpl)
                .also {
                    INSTANCE = it
                }
    }
}
