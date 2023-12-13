package com.carmabs.ema.core.viewmodel

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.action.EmaActionDispatcher
import com.carmabs.ema.core.concurrency.EmaMainScope
import com.carmabs.ema.core.model.emaFlowSingleEvent
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * Created by Carlos Mateo Benito on 14/04/2023.
 *
 * Viewmodel that handles actions by the method onAction(action:EmaAction).
 * It centralize the user actions on view.Based on MVI
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
abstract class EmaViewModelAction<S : EmaDataState, D : EmaNavigationEvent, A : EmaAction.Screen>(
    initialDataState: S,
    scope: CoroutineScope = EmaMainScope()
) : EmaViewModelBasic<S, D>(initialDataState, scope), EmaActionDispatcher<A> {

    private val observableAction = emaFlowSingleEvent<A>()

    /**
     * Subscribe to the actions dispatched to this ViewModel
     */
    override fun subscribeToActions(): Flow<A> = observableAction

    final override fun onAction(action: A) {
        onFeatureAction(action)
        observableAction.tryEmit(action)
    }

    abstract fun onFeatureAction(action: A)
}