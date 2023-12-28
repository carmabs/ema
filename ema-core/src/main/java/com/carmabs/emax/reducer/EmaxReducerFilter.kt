package com.carmabs.emax.reducer

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaState
import kotlin.reflect.KClass
import kotlin.reflect.cast
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
interface EmaxReducerFilter<S : EmaDataState, A : EmaAction>: EmaxReducer<S> {

    val actionFilter: KClass<out A>
    override fun reduce(state: S, action: EmaAction): S {
        return action.takeIf { it::class.isSubclassOf(actionFilter) }?.let {
            state.onReduce(actionFilter.cast(action))
        } ?: state
    }
    fun S.onReduce(action: A): S
}
fun <S : EmaDataState, A : EmaAction> emaxReducerOf(
    action: KClass<out A>,
    reduceAction: S.(A) -> S
) = object : EmaxReducerFilter<S,A> {
    override fun S.onReduce(action: A): S {
        return reduceAction(action)
    }

    override val actionFilter: KClass<out A> = action
}