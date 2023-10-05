package com.carmabs.emax.middleware.common

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.state.EmaDataState

/**
 * Created by Carlos Mateo Benito on 29/9/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
interface EmaxMiddleware<S : EmaDataState> {

    context (MiddlewareScope<S>)
    operator fun invoke(
        action: EmaAction,
        next: EmaNextMiddleware,
    ): EmaNextMiddlewareResult
}