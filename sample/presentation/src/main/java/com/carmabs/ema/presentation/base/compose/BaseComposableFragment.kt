package com.carmabs.ema.presentation.base.compose

import androidx.compose.runtime.Composable
import com.carmabs.ema.compose.ui.EmaComposableFragment
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.viewmodel.EmaViewModel


/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

abstract class BaseComposableFragment<S : EmaDataState, VM : EmaViewModel<S, N>, N : EmaNavigationEvent> :
    EmaComposableFragment<S, VM, N>() {

    @Composable
    override fun onStateNormal(data: S) {
        onNormal(data = data)
    }

    @Composable
    override fun onStateOverlapped(data: EmaExtraData) {
        onOverlapped(data = data)
    }

    final override fun onSingleEvent(extra: EmaExtraData) {
        onSingle(extra)
    }


    @Composable
    abstract fun onNormal(data: S)

    @Composable
    protected open fun onOverlapped(data: EmaExtraData) = Unit

    protected open fun onSingle(data: EmaExtraData) = Unit

}