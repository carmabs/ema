package com.carmabs.ema.core.viewmodel.emux.store

import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.emux.middleware.common.EmaMiddleware
import com.carmabs.ema.core.viewmodel.emux.reducer.EmaReducer

/**
 * Created by Carlos Mateo Benito on 29/9/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class EmaStoreSetupScope<S : EmaDataState> internal constructor() {

    internal var reducersList: List<EmaReducer<S>> = emptyList()
        private set
    internal var middlewareList: List<EmaMiddleware<S>> = emptyList()
        private set

    fun addMiddleware(vararg middleware: EmaMiddleware<S>) {
        val mMiddlewareList = middlewareList.toMutableList()
        middleware.forEach {
            mMiddlewareList.add(it)
        }
        middlewareList = mMiddlewareList.toList()
    }

    fun addReducer(vararg reducer: EmaReducer<S>) {
        val mReducerList = reducersList.toMutableList()
        reducer.forEach {
            mReducerList.add(it)
        }
        reducersList = mReducerList.toList()
    }
}