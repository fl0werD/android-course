package com.example.fl0wer.domain.core.dispatchers

import com.example.fl0wer.domain.core.dispatchers.DispatchersProvider
import kotlinx.coroutines.Dispatchers

object DispatchersProviderImpl : DispatchersProvider {
    override val default = Dispatchers.Default
    override val main = Dispatchers.Main.immediate
    override val unconfined = Dispatchers.Unconfined
    override val io = Dispatchers.IO
}
