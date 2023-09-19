package com.carmabs.ema.android.viewmodel

import com.carmabs.ema.core.navigator.EmaEmptyNavigationEvent
import com.carmabs.ema.core.state.EmaEmptyState
import com.carmabs.ema.core.viewmodel.EmaEmptyViewModel

/**
 * Created by Carlos Mateo Benito on 13/02/2021.
 *
 * <p>
 * Copyright (c) 2021 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
object EmaEmptyAndroidViewModel : EmaAndroidViewModel<EmaEmptyState,EmaEmptyNavigationEvent>(EmaEmptyViewModel)