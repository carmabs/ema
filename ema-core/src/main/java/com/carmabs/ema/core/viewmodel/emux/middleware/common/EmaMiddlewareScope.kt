package com.carmabs.ema.core.viewmodel.emux.middleware.common

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.emux.store.EmaStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
class MiddlewareScope<S : EmaDataState>(
    private val store: EmaStore<S>,
    private val scope: CoroutineScope
) {
    val state: S
        get() = store.state

    private val sideEffectScope = SideEffectScope(store)

    fun sideEffect(
        effect: @MiddlewareScopeDsl suspend  SideEffectScope<S>.(CoroutineScope) -> Unit
    ) {
        scope.launch {
            effect.invoke(sideEffectScope,this)
        }
    }


}

@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
annotation class SideEffectScopeDsl

@SideEffectScopeDsl
class SideEffectScope<S : EmaDataState>(
    private val store: EmaStore<S>
) {
    val state: S
        get() = store.state

    fun dispatch(
        emaAction: EmaAction
    ) {
        store.dispatch(emaAction)
    }
}