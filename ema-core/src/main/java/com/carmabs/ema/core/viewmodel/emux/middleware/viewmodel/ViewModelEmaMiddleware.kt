package com.carmabs.ema.core.viewmodel.emux.middleware.viewmodel

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.action.FeatureEmaAction
import com.carmabs.ema.core.model.EmaEvent
import com.carmabs.ema.core.navigator.EmaNavigationDirectionEvent
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaResultHandler
import com.carmabs.ema.core.viewmodel.emux.EmaViewModelScope
import com.carmabs.ema.core.viewmodel.emux.middleware.common.EmaMiddleware
import com.carmabs.ema.core.viewmodel.emux.middleware.common.EmaNextMiddleware
import com.carmabs.ema.core.viewmodel.emux.middleware.common.EmaNextMiddlewareResult
import com.carmabs.ema.core.viewmodel.emux.middleware.common.MiddlewareScope
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
class ViewModelEmaMiddleware<S : EmaDataState, D : EmaNavigationEvent, A : FeatureEmaAction> internal constructor(
    private val resultHandler: EmaResultHandler,
    private val viewModelId: String,
    private val navigationState: MutableSharedFlow<EmaNavigationDirectionEvent>,
    private val observableSingleEvent: MutableSharedFlow<EmaEvent>,
    private val onViewModelAction:  EmaViewModelScope<S,D>.(action: A) -> EmaAction
) : EmaMiddleware<S> {


    context(MiddlewareScope<S>)
    override fun invoke(
        action: EmaAction,
        next: EmaNextMiddleware
    ): EmaNextMiddlewareResult {
        return when (action) {
            is FeatureEmaAction -> {
                next(
                    (action as? A)?.let { featureAction ->
                       val viewModelScope = EmaViewModelScope<S,D>(resultHandler, viewModelId, navigationState, observableSingleEvent,this@MiddlewareScope)
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