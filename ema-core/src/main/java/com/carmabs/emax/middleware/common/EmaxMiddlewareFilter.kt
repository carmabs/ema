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
abstract class EmaxMiddlewareFilter<S : EmaDataState, A : EmaAction, F : EmaAction>(
    private val actionFilter: KClass<out F>,
) : EmaxMiddleware<S, A> {
    context(MiddlewareScope<S, A>)
    override fun invoke(
        action: EmaAction,
        next: NextMiddleware
    ) {
        if (action::class.isSubclassOf(actionFilter)) {
            onAction(actionFilter.cast(action))
        }
        next(action)
    }

    context(MiddlewareScope<S, A>)
    abstract fun onAction(action: F)
}

fun <S : EmaDataState, A : EmaAction, F : EmaAction> emaxMiddlewareOf(
    actionFilter: KClass<F>,
    middlewareAction: MiddlewareScope<S, A>.(F) -> Unit
) = object : EmaxMiddlewareFilter<S, A, F>(actionFilter) {

    context(MiddlewareScope<S, A>)
    override fun onAction(action: F) {
        middlewareAction(this@MiddlewareScope, action)
    }
}