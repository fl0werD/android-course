package com.example.fl0wer.androidApp.di

import android.content.Context
import com.example.fl0wer.androidApp.data.contacts.ContactsRepositoryImpl
import com.example.fl0wer.androidApp.data.contacts.ReminderRepositoryImpl
import com.example.fl0wer.androidApp.data.core.network.GoogleApi
import com.example.fl0wer.androidApp.data.directions.DirectionRepositoryImpl
import com.example.fl0wer.androidApp.data.locations.LocationRepositoryImpl
import com.example.fl0wer.androidApp.data.locations.database.LocationDao
import com.example.fl0wer.androidApp.domain.contacts.ContactsInteractor
import com.example.fl0wer.androidApp.domain.contacts.ContactsInteractorImpl
import com.example.fl0wer.androidApp.domain.contacts.ContactsRepository
import com.example.fl0wer.androidApp.domain.contacts.ReminderInteractor
import com.example.fl0wer.androidApp.domain.contacts.ReminderInteractorImpl
import com.example.fl0wer.androidApp.domain.contacts.ReminderRepository
import com.example.fl0wer.androidApp.ui.core.dispatchers.DispatchersProvider
import com.example.fl0wer.androidApp.domain.directions.DirectionInteractor
import com.example.fl0wer.androidApp.domain.directions.DirectionInteractorImpl
import com.example.fl0wer.androidApp.domain.directions.DirectionRepository
import com.example.fl0wer.androidApp.domain.locations.LocationInteractor
import com.example.fl0wer.androidApp.domain.locations.LocationInteractorImpl
import com.example.fl0wer.androidApp.domain.locations.LocationRepository
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
        locationRepository: LocationRepository,
        dispatchersProvider: DispatchersProvider,
    ): ContactsInteractor = ContactsInteractorImpl(
        contactsRepository,
        locationRepository,
        dispatchersProvider,
    )

    @[Provides Singleton]
    fun bindReminderRepository(
        context: Context,
        dispatchersProvider: DispatchersProvider,
    ): ReminderRepository = ReminderRepositoryImpl(
        context,
        dispatchersProvider,
    )

    @[Provides Singleton]
    fun bindReminderInteractor(
        reminderRepository: ReminderRepository,
        dispatchersProvider: DispatchersProvider,
    ): ReminderInteractor = ReminderInteractorImpl(
        reminderRepository,
        GregorianCalendar(),
        dispatchersProvider,
    )

    @[Provides Singleton]
    fun bindLocationRepository(
        locationDao: LocationDao,
        googleApi: GoogleApi,
        dispatchersProvider: DispatchersProvider,
    ): LocationRepository = LocationRepositoryImpl(
        locationDao,
        googleApi,
        dispatchersProvider,
    )

    @[Provides Singleton]
    fun bindLocationInteractor(
        locationRepository: LocationRepository,
        dispatchersProvider: DispatchersProvider,
    ): LocationInteractor = LocationInteractorImpl(
        locationRepository,
        dispatchersProvider,
    )

    @[Provides Singleton]
    fun bindDirectionRepository(
        googleApi: GoogleApi,
        dispatchersProvider: DispatchersProvider,
    ): DirectionRepository = DirectionRepositoryImpl(
        googleApi,
        dispatchersProvider,
    )

    @[Provides Singleton]
    fun bindDirectionInteractor(
        directionRepository: DirectionRepository,
        dispatchersProvider: DispatchersProvider,
    ): DirectionInteractor = DirectionInteractorImpl(
        directionRepository,
        dispatchersProvider,
    )
}
