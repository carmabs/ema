package com.carmabs.emax.middleware.viewmodel

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.model.EmaEvent
import com.carmabs.ema.core.navigator.EmaNavigationDirection
import com.carmabs.ema.core.navigator.EmaNavigationDirectionEvent
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.emax.EmaxViewModelScope
import com.carmabs.emax.middleware.common.EmaNextMiddleware
import com.carmabs.emax.middleware.common.EmaNextMiddlewareResult
import com.carmabs.emax.middleware.common.EmaxMiddleware
import com.carmabs.emax.middleware.common.MiddlewareScope
import com.carmabs.emax.middleware.result.ResultWrapper
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Created by Carlos Mateo Benito on 1/10/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
@Suppress("UNCHECKED_CAST")
class ViewModelEmaxMiddleware<S : EmaDataState, A : EmaAction, N : EmaNavigationEvent> internal constructor(
    private val navigationState: MutableSharedFlow<EmaNavigationDirectionEvent>,
    private val observableSingleEvent: MutableSharedFlow<EmaEvent>,
    middlewareBuilder: SideEffectEmaxViewModelBuilder<S,A,N>.()->Unit
) : EmaxMiddleware<S> {

    private val builder = SideEffectEmaxViewModelBuilder<S,A,N>()
    private val resultWrapper = ResultWrapper()
    init {
        middlewareBuilder.invoke(builder)
    }

    context(MiddlewareScope<S>)
    override fun invoke(
        action: EmaAction,
        next: EmaNextMiddleware
    ): EmaNextMiddlewareResult {
        (action as? A)?.let {
            val viewModelScope = EmaxViewModelScope<S, N>(
                resultWrapper,
                navigationState,
                observableSingleEvent,
                this@MiddlewareScope
            )
            builder.applyListeners(action,viewModelScope)
        }
        return next(action)
    }
    fun onActionBackHardwarePressed() {
        navigationState.tryEmit(
            EmaNavigationDirectionEvent.Launched(
                EmaNavigationDirection.Back(
                    resultWrapper.backResult
                )
            )
        )
    }
}