package com.carmabs.ema.presentation.base

import androidx.viewbinding.ViewBinding
import com.carmabs.ema.android.ui.EmaFragment
import com.carmabs.ema.core.navigator.EmaDestination
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

abstract class BaseFragment<B : ViewBinding, S : EmaDataState, VM : EmaViewModel<S, D>, D : EmaDestination> :
    EmaFragment<B, S, VM, D>() {

    final override fun B.onStateNormal(data: S) {
        onNormal(data)
    }

    final override fun B.onStateOverlayed(data: EmaExtraData) {
        onOverlayed(data)
    }

    final override fun B.onSingleEvent(data: EmaExtraData) {
        onSingle(data)
    }

    abstract fun B.onNormal(data: S)

    protected open fun B.onOverlayed(data: EmaExtraData) {}

    protected open fun B.onSingle(data: EmaExtraData) {}

}