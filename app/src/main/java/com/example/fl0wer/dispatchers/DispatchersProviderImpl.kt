package com.example.fl0wer.dispatchers

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

object DispatchersProviderImpl : DispatchersProvider {
    override val default: Scheduler = Schedulers.computation()
    override val main: Scheduler = AndroidSchedulers.mainThread()
    override val io: Scheduler = Schedulers.io()
}
