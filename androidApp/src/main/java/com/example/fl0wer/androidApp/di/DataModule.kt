package com.example.fl0wer.androidApp.di

import android.content.Context
import com.example.fl0wer.androidApp.data.contacts.ContactsRepositoryImpl
import com.example.fl0wer.androidApp.data.contacts.ReminderRepositoryImpl
import com.example.fl0wer.androidApp.data.directions.DirectionRepositoryImpl
import com.example.fl0wer.androidApp.data.directions.network.DirectionsApi
import com.example.fl0wer.androidApp.data.locations.LocationRepositoryImpl
import com.example.fl0wer.androidApp.data.locations.database.LocationDao
import com.example.fl0wer.androidApp.data.locations.network.GeocodeApi
import com.example.fl0wer.domain.contacts.*
import com.example.fl0wer.domain.core.dispatchers.DispatchersProvider
import com.example.fl0wer.domain.directions.DirectionInteractor
import com.example.fl0wer.domain.directions.DirectionInteractorImpl
import com.example.fl0wer.domain.directions.DirectionRepository
import com.example.fl0wer.domain.locations.LocationInteractor
import com.example.fl0wer.domain.locations.LocationInteractorImpl
import com.example.fl0wer.domain.locations.LocationRepository
import dagger.Module
import dagger.Provides
import java.util.*
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
        geocodeApi: GeocodeApi,
    ): LocationRepository = LocationRepositoryImpl(
        locationDao,
        geocodeApi,
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
        directionsApi: DirectionsApi,
    ): DirectionRepository = DirectionRepositoryImpl(
        directionsApi,
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
