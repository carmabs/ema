package com.carmabs.ema.core.viewmodel.emux.reducer

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
interface EmaReducer<S : EmaDataState> {
    fun reduce(state: S, action: EmaAction): S
}