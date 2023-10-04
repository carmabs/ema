package com.carmabs.ema.core.viewmodel.emux.middleware.log

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.logging.toStringPretty
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.emux.middleware.common.EmaMiddleware
import com.carmabs.ema.core.viewmodel.emux.middleware.common.EmaNextMiddleware
import com.carmabs.ema.core.viewmodel.emux.middleware.common.EmaNextMiddlewareResult
import com.carmabs.ema.core.viewmodel.emux.middleware.common.MiddlewareScope
import java.util.logging.Logger

/**
 * Created by Carlos Mateo Benito on 2/10/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class EmaLoggerMiddleware<S : EmaDataState> : EmaMiddleware<S> {
    context(MiddlewareScope<S>) override fun invoke(
        action: EmaAction,
        next: EmaNextMiddleware
    ): EmaNextMiddlewareResult {
        val nextFunction = Logger.getLogger("EMA").run {
            info("////////////////////////////EMA STATE LOGGING////////////////////////////")
            info("DISPATCHED ACTION:\n\t${action.toStringPretty()}")
            info("STATE BEFORE APPLY ACTION:\n\t${state.toStringPretty()}")
            val nextFunction = next.invoke(action)
            info("STATE AFTER APPLY ACTION:\n\t${state.toStringPretty()}")
            info("/////////////////////////////////////////////////////////////////////////")
            nextFunction
        }
        return nextFunction
    }
}