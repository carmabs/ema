package com.carmabs.ema.core.viewmodel.emux.middleware

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.emux.store.EmaStore
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
class EmaMiddlewareStore<S : EmaDataState>(
    private val store: EmaStore<S>,
    private val scope: CoroutineScope,
    private var middlewares: List<EmaMiddleware<S>> = mutableListOf()
) {
    fun setMiddleware(vararg middleware: EmaMiddleware<S>) {
        middlewares = listOf(*middleware).reversed()
    }

    fun applyMiddleware(action: EmaAction, nextAction: (EmaAction) -> Unit) {

        middlewares.fold(nextAction) { previousAction, middle ->
            val middlewareScope = EmaMiddlewareScope(scope) {
                previousAction
            }
            middlewareScope.run {
                middle.invoke(
                    store,
                    action
                )
            }
        }.invoke(action)
    }
}
