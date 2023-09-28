package com.carmabs.ema.core.viewmodel.emux.middleware

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.action.ViewEmaAction
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.emux.store.EmaStore

/**
 * Created by Carlos Mateo Benito on 1/10/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class ViewEventEmaMiddleware<S : EmaDataState>(
    private val onViewAction: EmaMiddlewareScope.(viewAction: ViewEmaAction) -> EmaNext
) : EmaMiddleware<S> {
    context(EmaMiddlewareScope)
    override fun invoke(
        store: EmaStore<S>,
        action: EmaAction,
    ): EmaNext {
        return when (action) {
            is ViewEmaAction -> {
                {
                    onViewAction.invoke(
                        this@EmaMiddlewareScope,
                        action
                    )
                }
            }
            else ->
                next(action)
        }
    }

}