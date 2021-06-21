package com.example.fl0wer.androidApp.data.contacts.receiver

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.plus
import timber.log.Timber

object AppScope {
    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        Timber.e(t)
    }
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main) + exceptionHandler
}
