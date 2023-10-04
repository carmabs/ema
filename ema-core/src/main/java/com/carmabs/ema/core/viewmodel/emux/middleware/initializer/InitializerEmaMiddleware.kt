package com.carmabs.ema.core.viewmodel.emux.middleware.initializer

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.emux.middleware.common.EmaMiddleware
import com.carmabs.ema.core.viewmodel.emux.middleware.common.EmaNextMiddleware
import com.carmabs.ema.core.viewmodel.emux.middleware.common.EmaNextMiddlewareResult
import com.carmabs.ema.core.viewmodel.emux.middleware.common.MiddlewareScope

/**
 * Created by Carlos Mateo Benito on 1/10/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class InitializerEmaMiddleware<S : EmaDataState>(
    private val onInitializerAction: MiddlewareScope<S>.(initializer: EmaInitializer) -> EmaAction
) : EmaMiddleware<S> {
    context(MiddlewareScope<S>)
    override fun invoke(
        action: EmaAction,
        next: EmaNextMiddleware
    ): EmaNextMiddlewareResult {
        return when (action) {
            is EmaInitializer -> {
               next(
                    onInitializerAction.invoke(
                        this@MiddlewareScope,
                        action
                    )
                )
            }
            else ->
                next(action)
        }
    }

}