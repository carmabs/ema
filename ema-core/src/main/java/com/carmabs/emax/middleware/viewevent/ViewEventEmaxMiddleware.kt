package com.carmabs.emax.middleware.viewevent

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.action.ViewEmaAction
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.emax.middleware.common.EmaxMiddleware
import com.carmabs.emax.middleware.common.EmaNextMiddleware
import com.carmabs.emax.middleware.common.EmaNextMiddlewareResult
import com.carmabs.emax.middleware.common.MiddlewareScope

/**
 * Created by Carlos Mateo Benito on 1/10/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class ViewEventEmaxMiddleware<S : EmaDataState> (
    private val onViewAction: MiddlewareScope<S>.(viewAction: ViewEmaAction) -> EmaAction
) : EmaxMiddleware<S> {
    context(MiddlewareScope<S>)
    override fun invoke(
        action: EmaAction,
        next: EmaNextMiddleware
    ): EmaNextMiddlewareResult {
        return when (action) {
            is ViewEmaAction -> {
                next(
                    onViewAction.invoke(
                        this@MiddlewareScope,
                        action
                    )
                )
            }
            else ->
                next.invoke(action)
        }
    }
}