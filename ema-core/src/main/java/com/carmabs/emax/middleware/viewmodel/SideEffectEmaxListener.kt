package com.carmabs.emax.middleware.viewmodel

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.emax.EmaxViewModelScope

/**
 * Created by Carlos Mateo Benito on 29/11/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
data class SideEffectEmaxListener<S: EmaDataState,A: EmaAction,N: EmaNavigationEvent>(
    val action:Class<*>,
    val scopedAction: EmaxViewModelScope<S, N>.(A)->Unit
)