package com.carmabs.ema.core.extension

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration


suspend inline fun <T> suspendCoroutineWithTimeout(
    duration: Duration,
    crossinline block: (CancellableContinuation<T>) -> Unit
) = withTimeout(duration) {
    suspendCancellableCoroutine(block = block)
}