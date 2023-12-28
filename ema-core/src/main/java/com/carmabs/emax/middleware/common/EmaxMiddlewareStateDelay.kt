package com.carmabs.emax.middleware.common

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaState
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
abstract class EmaxMiddlewareStateDelay<S : EmaDataState, A : EmaAction, N : EmaNavigationEvent, F : EmaAction>(
    actionDelayed: KClass<F>,
    delay: Duration
) : EmaxMiddlewareDelay<EmaState<S,N>,A,F>(actionDelayed, delay)

fun <S : EmaDataState, A : EmaAction, N:EmaNavigationEvent,F : EmaAction> emaxMiddlewareDelayStateOf(
    actionDelayed: KClass<F>,
    delay: Duration,
    onBeforeDelayed: ((F) -> Unit)? = null
) = emaxMiddlewareDelayOf<EmaState<S,N>,A,F>(actionDelayed, delay, onBeforeDelayed)