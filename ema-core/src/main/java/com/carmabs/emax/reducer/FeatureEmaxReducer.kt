package com.carmabs.emax.reducer

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.action.FeatureEmaAction
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaState

/**
 * Created by Carlos Mateo Benito on 1/10/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class FeatureEmaxReducer<S : EmaDataState, A : FeatureEmaAction> internal constructor(
    initialState: EmaState<S>,
    private val reducerAction: EmaFeatureReducerScope<S>.(S, A) -> S,
    private val onStateUpdate: (EmaState<S>) -> Unit
) : EmaxReducer<S> {

    private val featureReducerScope = EmaFeatureReducerScope(initialState)
    override fun reduce(state: S, action: EmaAction): S {
        return when (action) {
            is FeatureEmaAction -> {
                (action as? A)?.let {
                    val newState = featureReducerScope.run {
                        reducerAction(state, it)
                    }
                    onStateUpdate.invoke(featureReducerScope.state)
                    newState
                } ?: state
            }

            else ->
                state
        }
    }
}