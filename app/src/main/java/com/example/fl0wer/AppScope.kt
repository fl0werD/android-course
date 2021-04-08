package com.example.fl0wer

import kotlinx.coroutines.*
import timber.log.Timber

object AppScope {
    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        Timber.e(t)
    }
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main) + exceptionHandler
}
