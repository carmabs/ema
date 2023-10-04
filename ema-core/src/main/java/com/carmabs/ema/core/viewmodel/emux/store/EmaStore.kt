package com.carmabs.ema.core.viewmodel.emux.store

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.constants.INT_ONE
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.emux.middleware.common.EmaMiddlewareStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn

/**
 * Created by Carlos Mateo Benito on 29/9/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class EmaStore<S : EmaDataState>(
    initialState: S,
    scope: CoroutineScope,
    setup: EmaStoreSetupScope<S>.() -> Unit
) {
    private val storeSetupScope = EmaStoreSetupScope<S>()

    init {
        storeSetupScope.setup()
    }

    var state: S = initialState
        private set(value)  {
            field = value
            //observableState.tryEmit(value)
        }

    private val middleWareStore: EmaMiddlewareStore<S> =
        EmaMiddlewareStore(this, scope, storeSetupScope.middlewareList)

    private val channelAction = Channel<EmaAction>()

    private val observableAction = channelAction.receiveAsFlow()

    val observableState: Flow<S> = observableAction
        .map { action ->
            storeSetupScope.reducersList.fold(state) { previousState, reducer ->
                reducer.reduce(previousState, action)
            }
        }
        .shareIn(scope, SharingStarted.Eagerly, replay = INT_ONE)

    //MutableSharedFlow(1,0, onBufferOverflow = BufferOverflow.DROP_OLDEST) /*

    fun dispatch(action: EmaAction) {
        val middlewareAction = middleWareStore.applyMiddleware(action)
        channelAction.trySend(middlewareAction)

    }
}