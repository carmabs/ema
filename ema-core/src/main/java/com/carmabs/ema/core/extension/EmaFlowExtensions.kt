package com.carmabs.ema.core.extension

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onCompletion

suspend inline fun <T, reified R> Flow<T>.untilInstanceOf(clazz:Class<R>) : R {
    return first { it is R } as R
}

suspend inline fun <T> Flow<T>.until(noinline condition: (T)->Boolean) : T {
    return first(condition)
}

fun <T> Flow<T>.concat(flow: Flow<T>) : Flow<T> {
    return onCompletion { emitAll(flow) }
}