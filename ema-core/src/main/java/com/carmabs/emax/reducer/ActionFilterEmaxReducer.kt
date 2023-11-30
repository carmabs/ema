package com.carmabs.emax.reducer

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.state.EmaDataState
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * Created by Carlos Mateo Benito on 1/10/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class ActionFilterEmaxReducer<S : EmaDataState, A : EmaAction>(
    private val filterClass:KClass<A>,
    private val reduceAction: S.(A) -> S
) : EmaxReducer<S> {

    override fun reduce(state: S, action: EmaAction): S {
        return action.takeIf { it::class.isSubclassOf(filterClass) }?.let {
            state.reduceAction(it as A)
        } ?: state
    }
}