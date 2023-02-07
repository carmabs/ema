package com.carmabs.ema.presentation.base

import com.carmabs.ema.core.navigator.EmaNavigationState
import com.carmabs.ema.core.viewmodel.EmaViewModel

/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

abstract class BaseViewModel<S:Any, D : EmaNavigationState> : EmaViewModel<S, D>(){

    override fun onViewResumed() {
        //Override if you want to do some task everytime the view goes
        //to foreground
    }
}