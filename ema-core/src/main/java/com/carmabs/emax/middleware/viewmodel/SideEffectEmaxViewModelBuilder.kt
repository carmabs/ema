package com.carmabs.emax.middleware.viewmodel

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.emax.EmaxViewModelScope
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
class SideEffectEmaxViewModelBuilder<S : EmaDataState, A : EmaAction, N : EmaNavigationEvent> {

    private val listeners = mutableListOf<SideEffectEmaxListener<S, A, N>>()
    internal fun applyListeners(action: A, emaxViewModelScope: EmaxViewModelScope<S, N>) {
        listeners.forEach { sideEffect ->
            if (action::class.isSubclassOf(sideEffect.action))
                sideEffect.scopedAction.invoke(emaxViewModelScope, action)
        }
    }

    fun registerActionListener(
        listener: SideEffectEmaxListener<S, A, N>
    ) {
        listeners.add(listener)
    }

    fun registerActionListener(
        action: KClass<out A>,
        listener: EmaxViewModelScope<S, N>.(A) -> Unit
    ) {
        listeners.add(SideEffectEmaxListener(action, listener))
    }

}

