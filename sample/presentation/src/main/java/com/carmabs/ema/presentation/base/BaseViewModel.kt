package com.carmabs.ema.presentation.base

import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaViewModel

/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

abstract class BaseViewModel<S:EmaDataState, D : EmaDestination> : EmaViewModel<S, D>()