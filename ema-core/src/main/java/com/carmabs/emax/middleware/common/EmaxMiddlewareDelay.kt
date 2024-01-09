package com.carmabs.emax.middleware.common

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.state.EmaDataState
import kotlinx.coroutines.delay
import kotlin.reflect.KClass
import kotlin.reflect.cast
import kotlin.reflect.full.isSubclassOf
import kotlin.time.Duration

/**
 * Created by Carlos Mateo Benito on 29/9/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
abstract class EmaxMiddlewareDelay<S : EmaDataState, A : EmaAction, F : EmaAction>(
    private val actionDelayed: KClass<F>,
    private val delay: Duration
) : EmaxMiddleware<S, A> {
    context(MiddlewareScope<S, A>)
    override fun invoke(
        action: EmaAction,
        next: NextMiddleware
    ) {
        if (action::class.isSubclassOf(this.actionDelayed)) {
            sideEffect {
                delay(delay)
                onBeforeDelayed(actionDelayed.cast(action))
                next(action)

            }
        } else {
            next(action)
        }
    }

    open suspend fun onBeforeDelayed(action: F) = Unit
}

fun <S : EmaDataState, A : EmaAction, F : EmaAction> emaxMiddlewareDelayOf(
    actionDelayed: KClass<F>,
    delay: Duration,
    onBeforeDelayed: ((F) -> Unit)? = null
) = object : EmaxMiddlewareDelay<S, A, F>(actionDelayed, delay) {

    override suspend fun onBeforeDelayed(action: F) {
        onBeforeDelayed?.also { it.invoke(action) }
    }
}