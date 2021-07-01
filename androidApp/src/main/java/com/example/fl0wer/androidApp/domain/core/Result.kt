package com.example.fl0wer.androidApp.domain.core

sealed class Result<out T> {
    data class Success<out T>(val value: T) : Result<T>()
    data class Failure(val throwable: Throwable) : Result<Nothing>()
}
