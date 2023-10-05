package com.carmabs.emax.middleware.viewmodel

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.action.FeatureEmaAction
import com.carmabs.ema.core.model.EmaEvent
import com.carmabs.ema.core.navigator.EmaNavigationDirectionEvent
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaResultHandler
import com.carmabs.emax.EmaxViewModelScope
import com.carmabs.emax.middleware.common.EmaxMiddleware
import com.carmabs.emax.middleware.common.EmaNextMiddleware
import com.carmabs.emax.middleware.common.EmaNextMiddlewareResult
import com.carmabs.emax.middleware.common.MiddlewareScope
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
class ViewModelEmaxMiddleware<S : EmaDataState, A : FeatureEmaAction, D : EmaNavigationEvent> internal constructor(
    private val resultHandler: EmaResultHandler,
    private val viewModelId: String,
    private val navigationState: MutableSharedFlow<EmaNavigationDirectionEvent>,
    private val observableSingleEvent: MutableSharedFlow<EmaEvent>,
    private val onViewModelAction: EmaxViewModelScope<S, D>.(action: A) -> A
) : EmaxMiddleware<S> {


    context(MiddlewareScope<S>)
    override fun invoke(
        action: EmaAction,
        next: EmaNextMiddleware
    ): EmaNextMiddlewareResult {
        return when (action) {
            is FeatureEmaAction -> {
                next(
                    (action as? A)?.let { featureAction ->
                        val viewModelScope = EmaxViewModelScope<S, D>(
                            resultHandler,
                            viewModelId,
                            navigationState,
                            observableSingleEvent,
                            this@MiddlewareScope
                        )
                        onViewModelAction.invoke(
                            viewModelScope,
                            featureAction
                        )
                    } ?: action
                )
            }

            else ->
                next(action)
        }
    }

}