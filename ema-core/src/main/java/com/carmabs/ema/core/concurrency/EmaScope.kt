package com.carmabs.ema.core.concurrency

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


/**
 * Change the dispatcher where the EMA internal functions call main corroutines.
 * Use only for testing
 */
fun EmaMainScope(): CoroutineScope =
    CoroutineScope(SupervisorJob() + EmaScopeDispatcher.mainDispatcher)

object EmaScopeDispatcher {
    var mainDispatcher: CoroutineDispatcher = Dispatchers.Main
        private set

    internal fun changeEmaMainDispatcher(dispatcher: CoroutineDispatcher) {
        mainDispatcher = dispatcher
    }
}




