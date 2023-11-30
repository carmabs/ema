package com.carmabs.emax.reducer

import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaState

/**
 * Created by Carlos Mateo Benito on 29/9/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class ViewModelStateEmaxReducerScope<S : EmaDataState> internal constructor(
    initialState: EmaState<S>
) {
    var state: EmaState<S> = initialState
        private set

    fun S.normal():S {
        state = EmaState.Normal(this)
        return this
    }

    fun S.overlapped(extraData: EmaExtraData):S {
        state = EmaState.Overlapped(this, extraData)
        return this
    }

}