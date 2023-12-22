package com.carmabs.emax.middleware.common

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.state.EmaDataState
import kotlin.reflect.KClass
import kotlin.reflect.cast
import kotlin.reflect.full.isSubclassOf

/**
 * Created by Carlos Mateo Benito on 29/9/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class EmaxMiddlewareActionFilter<A : EmaAction, S : EmaDataState>(
    private val filterClass: KClass<out A>,
    private val middlewareAction: MiddlewareScope<A, S>.(A, NextMiddleware) -> EmaxNextMiddlewareResult
) : EmaxMiddleware<A, S> {
    context(MiddlewareScope<A, S>)
    override fun invoke(
        action: EmaAction,
        next: NextMiddleware
    ): EmaxNextMiddlewareResult {
        return if (action::class.isSubclassOf(filterClass)) {
            this@MiddlewareScope.middlewareAction(filterClass.cast(action), next)
        } else
            next(action)
    }
}