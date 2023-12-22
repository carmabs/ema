package com.carmabs.emax.middleware.common

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.emax.store.EmaxStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Created by Carlos Mateo Benito on 4/10/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
annotation class MiddlewareScopeDsl

@MiddlewareScopeDsl
class MiddlewareScope<A : EmaAction, S : EmaDataState> internal constructor(
    private val store: EmaxStore<S>,
    private val scope: CoroutineScope
) {
    val state: S
        get() = store.state

    private val sideEffectScope = SideEffectScope<A, S>(store, scope)

    fun sideEffect(
        effect: @MiddlewareScopeDsl suspend SideEffectScope<A, S>.() -> Unit
    ): Job {
        return scope.launch {
            effect.invoke(sideEffectScope)
        }
    }
}