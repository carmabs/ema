package com.carmabs.emax.store

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.emax.middleware.common.EmaxMiddleware
import com.carmabs.emax.middleware.common.MiddlewareScope
import com.carmabs.emax.middleware.common.emaxMiddlewareOf
import com.carmabs.emax.reducer.EmaxReducer
import kotlin.reflect.KClass

/**
 * Created by Carlos Mateo Benito on 29/9/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class StoreSetupScope<S : EmaDataState> internal constructor() {

    internal var reducersList: List<EmaxReducer<S>> = emptyList()
        private set
    internal var middlewareList: List<EmaxMiddleware<S,EmaAction>> = emptyList()
        private set

    fun addMiddleware(vararg middleware: EmaxMiddleware<S,EmaAction>) {
        val mMiddlewareList = middlewareList.toMutableList()
        middleware.forEach {
            mMiddlewareList.add(it)
        }
        middlewareList = mMiddlewareList.toList()
    }

    fun <F : EmaAction> addMiddleware(
        filter: KClass<F>,
        middlewareAction: MiddlewareScope<S,EmaAction>.(F) -> Unit
    ) {
        val mMiddlewareList = middlewareList.toMutableList()
        mMiddlewareList.add(emaxMiddlewareOf(filter, middlewareAction))
        middlewareList = mMiddlewareList.toList()
    }

    fun addReducer(vararg reducer: EmaxReducer<S>) {
        val mReducerList = reducersList.toMutableList()
        reducer.forEach {
            mReducerList.add(it)
        }
        reducersList = mReducerList.toList()
    }
}