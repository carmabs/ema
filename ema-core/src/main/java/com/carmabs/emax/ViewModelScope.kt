package com.carmabs.emax

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.model.EmaEvent
import com.carmabs.ema.core.navigator.EmaNavigationDirection
import com.carmabs.ema.core.navigator.EmaNavigationDirectionEvent
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.emax.middleware.common.MiddlewareScope
import com.carmabs.emax.middleware.common.SideEffectScope
import com.carmabs.emax.middleware.result.ResultWrapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Created by Carlos Mateo Benito on 29/9/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
@DslMarker
@Target(AnnotationTarget.CLASS)
annotation class ViewModelScopeDsl

@ViewModelScopeDsl
class ViewModelScope<in A : EmaAction, S : EmaDataState, in D : EmaNavigationEvent> internal constructor(
    private val resultWrapper: ResultWrapper,
    private val navigationState: MutableSharedFlow<EmaNavigationDirectionEvent>,
    private val observableSingleEvent: MutableSharedFlow<EmaEvent>,
    private val middlewareScope: MiddlewareScope<A, S>
) {
    /**
     * Method use to notify a navigation event
     * @param navigation The object that represent the destination of the navigation
     */
    fun navigate(navigation: D) {
        navigationState.tryEmit(
            EmaNavigationDirectionEvent.Launched(
                EmaNavigationDirection.Forward(
                    navigation
                )
            )
        )
    }

    val state
        get() = middlewareScope.state

    fun launch(sideEffectAction: suspend (SideEffectScope<A, S>).() -> Unit): Job {
        return middlewareScope.sideEffect(sideEffectAction)
    }

    fun setBackResult(backResult: Any?) {
        resultWrapper.backResult = backResult
    }

    fun clearBackResult() {
        resultWrapper.backResult = null
    }

    /**
     * Method used to notify to the observer for a single event that will be notified only once time.
     * It a new observer is attached, it will not be notified
     */
    fun singleEvent(extraData: EmaExtraData) {
        observableSingleEvent.tryEmit(EmaEvent.Launched(extraData))
    }

}