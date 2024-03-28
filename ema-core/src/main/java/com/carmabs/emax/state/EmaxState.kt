package com.carmabs.emax.state

import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData

/**
 * Created by Carlos Mateo Benito on 24/12/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
sealed class EmaxState<T : EmaDataState, N : EmaNavigationEvent> private constructor(
    open val data: T,
    open val navigation: N
) : EmaDataState {

    var result:Any?=null

    /**
     * State that represents the current state of a view.
     * @constructor T is the state model of the view
     */
    data class Normal<T : EmaDataState, N : EmaNavigationEvent>(
        override val data: T,
        override val navigation: N
    ) : EmaxState<T, N>(data = data, navigation = navigation)

    /**
     * State that represents an overlapped state of a view.
     * @constructor T is the state model of the view, data represents the current state of the view, dataOverlated represents extra data to handle the overlapped state
     */
    data class Overlapped<T : EmaDataState, N : EmaNavigationEvent>(
        override val data: T,
        override val navigation: N,
        val extraData: EmaExtraData = EmaExtraData()
    ) :
        EmaxState<T, N>(data, navigation)

    fun update(updateAction: T.() -> T): EmaxState<T, N> {
        return when (this) {
            is Normal -> Normal(data.updateAction(), navigation)
            is Overlapped -> Overlapped(data.updateAction(), navigation, extraData)
        }
    }

    fun normal(updateAction: T.() -> T): EmaxState<T, N> {
        return Normal(data.updateAction(), navigation)
    }

    fun normal(): EmaxState<T, N> {
        return Normal(data, navigation)
    }

    fun overlapped(extraData: EmaExtraData = EmaExtraData()): EmaxState<T, N> {
        return Overlapped(this.data, navigation, extraData)
    }

    fun navigate(navigationEvent:N): EmaxState<T, N> {
        return when (this) {
            is Normal -> Normal(data, navigationEvent)
            is Overlapped -> Overlapped(data, navigationEvent, extraData)
        }
    }
}

