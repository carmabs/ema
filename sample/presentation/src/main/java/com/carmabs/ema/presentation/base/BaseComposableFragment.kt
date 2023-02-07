package com.carmabs.ema.presentation.base

import androidx.compose.runtime.Composable
import com.carmabs.ema.android.uicompose.EmaComposableFragment
import com.carmabs.ema.core.navigator.EmaNavigationState
import com.carmabs.ema.core.state.EmaBaseState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.viewmodel.EmaViewModel
import com.carmabs.ema.presentation.injection.fragmentInjection


/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

abstract class BaseComposableFragment<S : EmaBaseState, VM : EmaViewModel<S, D>, D : EmaNavigationState> :
    EmaComposableFragment<S, VM, D>() {

    override fun injectFragmentModule(kodein: DI.MainBuilder): DI.Module? = fragmentInjection(this)

    @Composable
    final override fun onStateNormalComposable(data: S) {
        onNormal(data)
    }

    @Composable
    final override fun onStateOverlayedComposable(data: EmaExtraData) {
        onOverlayed(data)
    }

    @Composable
    final override fun onStateErrorComposable(error: Throwable) {
        onError(error)
    }

    final override fun onSingleEvent(data: EmaExtraData) {
        onSingle(data)
    }


    @Composable
    abstract fun onNormal(data: S)

    @Composable
    protected open fun onOverlayed(data: EmaExtraData){}

    protected open fun onSingle(data: EmaExtraData){}

    @Composable
    protected open fun onError(error: Throwable){}

}