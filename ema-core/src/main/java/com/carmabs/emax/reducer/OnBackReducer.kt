package com.carmabs.emax.reducer

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaState

/**
 * Created by Carlos Mateo Benito on 24/12/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
internal fun <S : EmaDataState, N : EmaNavigationEvent> OnBackReducer() =
    emaxReducerOf<EmaState<S, N>, EmaAction.ViewModel>(
        EmaAction.ViewModel::class
    ) {
        when (it) {
            EmaAction.ViewModel.NavigationBack -> navigateBack()
            EmaAction.ViewModel.OnNavigated -> onNavigated()
            EmaAction.ViewModel.ConsumeSingleEvent -> consumeSingleEvent()
        }

    }