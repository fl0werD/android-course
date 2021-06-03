package com.example.fl0wer.di

import android.content.Context
import com.example.fl0wer.contacts.ContactsInteractor
import com.example.fl0wer.contacts.ContactsInteractorImpl
import com.example.fl0wer.contacts.ContactsRepository
import com.example.fl0wer.contacts.ReminderInteractor
import com.example.fl0wer.contacts.ReminderInteractorImpl
import com.example.fl0wer.contacts.ReminderRepository
import com.example.fl0wer.dispatchers.DispatchersProvider
import com.example.fl0wer.repository.ContactsRepositoryImpl
import com.example.fl0wer.repository.ReminderRepositoryImpl
import dagger.Module
import dagger.Provides
import java.util.GregorianCalendar
import javax.inject.Singleton

@Module
object DataModule {
    @[Provides Singleton]
    fun bindContactsRepository(
        context: Context,
        dispatchersProvider: DispatchersProvider,
    ): ContactsRepository = ContactsRepositoryImpl(
        context,
        dispatchersProvider,
    )

    @[Provides Singleton]
    fun bindContactsInteractor(
        contactsRepository: ContactsRepository,
        reminderRepository: ReminderRepository,
    ): ContactsInteractor = ContactsInteractorImpl(
        contactsRepository,
        reminderRepository,
    )

    @[Provides Singleton]
    fun bindReminderRepository(
        context: Context,
    ): ReminderRepository = ReminderRepositoryImpl(
        context,
    )

    @[Provides Singleton]
    fun bindReminderInteractor(
        reminderRepository: ReminderRepository,
    ): ReminderInteractor = ReminderInteractorImpl(
        reminderRepository,
        GregorianCalendar(),
    )
}
