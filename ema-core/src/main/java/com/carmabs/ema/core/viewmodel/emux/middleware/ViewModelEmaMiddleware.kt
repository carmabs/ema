package com.carmabs.ema.core.viewmodel.emux.middleware

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.action.FeatureEmaAction
import com.carmabs.ema.core.model.EmaEvent
import com.carmabs.ema.core.navigator.EmaNavigationDirectionEvent
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaResultHandler
import com.carmabs.ema.core.viewmodel.emux.EmaViewModelScope
import com.carmabs.ema.core.viewmodel.emux.store.EmaStore
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
    resultHandler: EmaResultHandler,
    viewModelId: String,
    navigationState: MutableSharedFlow<EmaNavigationDirectionEvent>,
    observableSingleEvent: MutableSharedFlow<EmaEvent>,
    private val onViewModelAction: context(EmaMiddlewareScope)EmaViewModelScope<D>.(action: A) -> Unit
) : EmaMiddleware<S> {

    private val sideEffectScope =
        EmaViewModelScope<D>(resultHandler, viewModelId, navigationState, observableSingleEvent)

    context(EmaMiddlewareScope)
    override fun invoke(
        store: EmaStore<S>,
        action: EmaAction,
    ): EmaNext {
        return when (action) {
            is FeatureEmaAction -> {
                (action as? A)?.let {
                    onViewModelAction.invoke(
                        this@EmaMiddlewareScope,
                        sideEffectScope,
                        it
                    )
                }
                next(action)
            }

            else ->
                next(action)
        }
    }

}