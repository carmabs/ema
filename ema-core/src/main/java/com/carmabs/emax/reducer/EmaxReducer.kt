package com.carmabs.emax.reducer

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.state.EmaDataState

/**
 * Created by Carlos Mateo Benito on 29/9/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
interface EmaxReducer<S : EmaDataState> {

    companion object{
        fun <S:EmaDataState>empty() =
           object : EmaxReducer<S>{
            override fun reduce(state: S, action: EmaAction): S {
                return state
            }
        }
    }
    fun reduce(state: S, action: EmaAction): S
}