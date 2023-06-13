package com.carmabs.ema.core.viewmodel

import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.navigator.EmaEmptyDestination
import com.carmabs.ema.core.state.EmaEmptyState

/**
 * Created by Carlos Mateo Benito on 21/7/22.
 *
 * <p>
 * Copyright (c) 2022 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
object EmaEmptyViewModel : EmaViewModel<EmaEmptyState, EmaEmptyDestination>(EmaEmptyState) {

    override val updateOnInitialization: Boolean
        get() = false

    override fun onStateCreated(initializer: EmaInitializer?) = Unit
}