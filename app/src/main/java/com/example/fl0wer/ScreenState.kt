package com.example.fl0wer

interface ScreenState

inline fun <reified S : ScreenState> ScreenState.nullOr(): S? =
    if (this is S) {
        this
    } else {
        null
    }
