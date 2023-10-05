package com.carmabs.emax.middleware.common

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.emax.store.EmaxStore
import kotlinx.coroutines.CoroutineScope

/**
 * Created by Carlos Mateo Benito on 29/9/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class EmaxMiddlewareStore<S : EmaDataState> internal constructor(
    private val store: EmaxStore<S>,
    private val scope: CoroutineScope,
    private var middlewares: List<EmaxMiddleware<S>> = mutableListOf()
) {
    fun setMiddleware(vararg middleware: EmaxMiddleware<S>) {
        middlewares = listOf(*middleware)
    }

    fun applyMiddleware(action: EmaAction, endAction: (EmaAction) -> Unit) {
        val end: EmaNextMiddleware = {
            endAction.invoke(it)
            EmaNextMiddlewareResult.CanceledAction
        }
        middlewares.reversed().fold(end) { nextAction, middle ->
            {
                MiddlewareScope(store, scope).run {
                    middle.invoke(it, nextAction)
                }
            }
        }.invoke(action)
    }


}
