package com.carmabs.emax.middleware.common

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaState
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
abstract class EmaxMiddlewareStateFilter<S : EmaDataState, A : EmaAction, N : EmaNavigationEvent, F : EmaAction>(
    actionFilter: KClass<out F>,
) : EmaxMiddlewareFilter<EmaState<S, N>, A, F>(actionFilter)


fun <S : EmaDataState, A : EmaAction, N : EmaNavigationEvent, F : EmaAction> emaxMiddlewareStateOf(
    actionFilter: KClass<F>,
    middlewareAction: MiddlewareScope<EmaState<S, N>, A>.(F) -> Unit
) = object : EmaxMiddlewareStateFilter<S, A,N,F>(actionFilter) {

    context(MiddlewareScope<EmaState<S, N>, A>)
    override fun onAction(action: F) {
        middlewareAction(this@MiddlewareScope, action)
    }
}