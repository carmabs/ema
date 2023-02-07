package com.carmabs.ema.presentation.base

import androidx.viewbinding.ViewBinding
import com.carmabs.ema.android.ui.EmaFragment
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
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

abstract class BaseFragment<B : ViewBinding, S : EmaBaseState, VM : EmaViewModel<S, D>, D : EmaNavigationState> :
    EmaFragment<B, S, VM, D>() {

    override fun injectFragmentModule(kodein: DI.MainBuilder): DI.Module? = fragmentInjection(this)

    override fun B.onStateNormal(data: S) {
        onNormal(data)
    }

    override fun B.onStateOverlayed(data: EmaExtraData) {
        onOverlayed(data)
    }

    override fun B.onSingleEvent(data: EmaExtraData) {
        onSingle(data)
    }

    override fun B.onStateError(error: Throwable) {
        onError(error)
    }

    abstract fun B.onNormal(data: S)

    protected open fun B.onOverlayed(data: EmaExtraData) {}

    protected open fun B.onSingle(data: EmaExtraData) {}

    protected open fun B.onError(error: Throwable) {}

}