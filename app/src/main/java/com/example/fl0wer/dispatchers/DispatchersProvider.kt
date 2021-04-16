package com.example.fl0wer.dispatchers

import io.reactivex.rxjava3.core.Scheduler

interface DispatchersProvider {
    val default: Scheduler
    val main: Scheduler
    val io: Scheduler
}
