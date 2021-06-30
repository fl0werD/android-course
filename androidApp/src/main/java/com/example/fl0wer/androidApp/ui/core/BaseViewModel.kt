package com.example.fl0wer.androidApp.ui.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fl0wer.androidApp.ui.UiState
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.back
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.plus
import timber.log.Timber

abstract class BaseViewModel<S : UiState>(
    initState: S,
    private val modo: Modo,
) : ViewModel() {
    private val _uiState = MutableStateFlow(initState)
    protected val uiState get() = _uiState.asStateFlow().value
    protected val vmScope = viewModelScope + CoroutineExceptionHandler { _, e ->
        if (e is CancellationException) {
            return@CoroutineExceptionHandler
        }
        Timber.e(e)
    }

    protected suspend fun updateState(newState: suspend () -> S) {
        _uiState.value = newState()
    }

    open fun backPressed() {
        modo.back()
    }

    open fun uiState() = _uiState
}
