package com.example.fl0wer.di.app

import android.content.Context
import com.example.fl0wer.dispatchers.DispatchersProvider
import com.example.fl0wer.repository.ContactsRepository
import com.example.fl0wer.repository.ContactsRepositoryImpl
import dagger.Module
import dagger.Provides
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
}
