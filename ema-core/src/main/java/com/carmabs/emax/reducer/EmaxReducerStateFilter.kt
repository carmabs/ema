package com.carmabs.emax.reducer

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaState
import kotlin.reflect.KClass

/**
 * Created by Carlos Mateo Benito on 1/10/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
interface EmaxReducerStateFilter<S : EmaDataState, A : EmaAction, N : EmaNavigationEvent> : EmaxReducerFilter<EmaState<S, N>, A>

fun <S : EmaDataState, A : EmaAction, N : EmaNavigationEvent> emaxReducerStateOf(
    action: KClass<out A>,
    reduceAction: EmaState<S, N>.(A) -> EmaState<S, N>
) = object : EmaxReducerStateFilter<S, A, N> {
    override fun EmaState<S, N>.onReduce(action: A): EmaState<S, N> {
        return reduceAction(action)
    }
    override val actionFilter: KClass<out A>
        get() = action
}