package com.example.fl0wer.androidApp.di

import android.content.Context
import androidx.room.Room
import com.example.fl0wer.BuildConfig
import com.example.fl0wer.androidApp.data.contacts.ContactsRepositoryImpl
import com.example.fl0wer.androidApp.data.contacts.ReminderRepositoryImpl
import com.example.fl0wer.androidApp.data.locations.LocationRepositoryImpl
import com.example.fl0wer.androidApp.data.locations.database.LocationDao
import com.example.fl0wer.androidApp.data.locations.database.LocationDatabase
import com.example.fl0wer.domain.contacts.ContactsInteractor
import com.example.fl0wer.domain.contacts.ContactsInteractorImpl
import com.example.fl0wer.domain.contacts.ContactsRepository
import com.example.fl0wer.domain.contacts.ReminderInteractor
import com.example.fl0wer.domain.contacts.ReminderInteractorImpl
import com.example.fl0wer.domain.contacts.ReminderRepository
import com.example.fl0wer.domain.core.dispatchers.DispatchersProvider
import com.example.fl0wer.domain.locations.LocationInteractor
import com.example.fl0wer.domain.locations.LocationInteractorImpl
import com.example.fl0wer.domain.locations.LocationRepository
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
    ): ContactsInteractor = ContactsInteractorImpl(
        contactsRepository,
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

    @[Provides Singleton]
    fun bindLocationRepository(
        locationDao: LocationDao,
    ): LocationRepository = LocationRepositoryImpl(
        locationDao,
    )

    @[Provides Singleton]
    fun bindLocationInteractor(
        locationRepository: LocationRepository,
        dispatchersProvider: DispatchersProvider,
    ): LocationInteractor = LocationInteractorImpl(
        locationRepository,
        dispatchersProvider,
    )
}
