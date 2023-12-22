package com.carmabs.emax.store

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.emax.middleware.common.MiddlewareStoreBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

/**
 * Created by Carlos Mateo Benito on 29/9/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class EmaxStore<S : EmaDataState>(
    initialState: S,
    scope: CoroutineScope,
    setup: StoreSetupScope<S>.() -> Unit
) {
    private val storeSetupScope = StoreSetupScope<S>()

    init {
        storeSetupScope.setup()
    }

    var state: S = initialState
        private set

    private val middleWareStore: MiddlewareStoreBuilder<S> =
        MiddlewareStoreBuilder(this, scope, storeSetupScope.middlewareList)

    private val channelAction = Channel<EmaAction>()

    private val observableAction = channelAction.receiveAsFlow()

    val observableState: Flow<S> = observableAction
        .map { action ->
            (storeSetupScope.reducersList.fold(state) { previousState, reducer ->
                reducer.reduce(previousState, action)
            }).also {
                state = it
            }
        }
        .stateIn(scope, SharingStarted.Eagerly,state)

    fun dispatch(action: EmaAction) {
        middleWareStore.applyMiddleware(action){
            channelAction.trySend(it)
        }


    }
}