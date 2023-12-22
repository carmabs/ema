package com.carmabs.emax.middleware.viewmodel

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.emax.ViewModelScope
import kotlin.reflect.KClass

/**
 * Created by Carlos Mateo Benito on 29/11/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
fun <F : EmaAction, S : EmaDataState, A : EmaAction, N : EmaNavigationEvent> emaxSideEffectListenerOf(
    action: KClass<out F>,
    scopedAction: ViewModelScope<A, S, N>.(F) -> Unit
) = object : EmaxSideEffect<F, S, A, N>(action) {

    override fun ViewModelScope<A, S, N>.onSideEffect(action: F) {
        scopedAction.invoke(this, action)
    }
}

abstract class EmaxSideEffect<F : EmaAction, S : EmaDataState, A : EmaAction, N : EmaNavigationEvent>(
    val action: KClass<out F>
) {
    abstract fun ViewModelScope<A, S, N>.onSideEffect(action: F)
}

interface EmaxSideEffectListener<in F : EmaAction, S : EmaDataState, A : EmaAction, N : EmaNavigationEvent> {
    fun ViewModelScope<A, S, N>.onSideEffect(action: F)
}