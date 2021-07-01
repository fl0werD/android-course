package com.example.fl0wer.androidApp.ui.core.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher

interface DispatchersProvider {
    val default: CoroutineDispatcher
    val main: MainCoroutineDispatcher
    val unconfined: CoroutineDispatcher
    val io: CoroutineDispatcher
}
