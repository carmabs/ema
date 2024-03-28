package com.carmabs.ema.core.extension

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration


suspend inline fun <T> suspendCoroutineWithTimeout(
    duration: Duration,
    crossinline block: (CancellableContinuation<T>) -> Unit
) = withTimeout(duration) {
    suspendCancellableCoroutine(block = block)
}

fun CoroutineContext.toScope() = CoroutineScope(this)