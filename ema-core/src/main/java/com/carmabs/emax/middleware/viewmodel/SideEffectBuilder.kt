package com.carmabs.emax.middleware.viewmodel

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.emax.ViewModelScope
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * Created by Carlos Mateo Benito on 29/11/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class SideEffectBuilder<S : EmaDataState, A : EmaAction, N : EmaNavigationEvent> internal constructor() {

    private val listeners = mutableListOf<Any>()
    internal fun applyListeners(
        action: EmaAction,
        viewModelScope: ViewModelScope<A, S, N>
    ) {
        listeners.forEach {
            val sideEffect = it as EmaxSideEffect<EmaAction, S, A, N>
            if (action::class.isSubclassOf(sideEffect.action))
                sideEffect.apply {
                    viewModelScope.onSideEffect(action)
                }
        }
    }

    fun <F : EmaAction> register(
        listener: EmaxSideEffect<F, S, A, N>
    ) {
        listeners.add(listener)
    }

    fun <F : EmaAction> register(
        action: KClass<out F>,
        listener: ViewModelScope<A, S, N>.(F) -> Unit
    ) {
        listeners.add(emaxSideEffectListenerOf(action, listener))
    }

    fun <F : EmaAction> registerListener(
        action: KClass<F>,
        listener: EmaxSideEffectListener<F, S, A, N>
    ) {
        listeners.add(emaxSideEffectListenerOf(action) {
            listener.apply {
                onSideEffect(it)
            }
        })
    }

}

