package com.carmabs.ema.presentation.ui.home

import com.carmabs.ema.core.constants.STRING_EMPTY
import com.carmabs.ema.core.state.EmaBaseState

/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 *
 * Created by: Carlos Mateo Benito on 20/1/19.
 */
data class EmaHomeState(
        val userName: String = STRING_EMPTY,
        val userPassword: String = STRING_EMPTY,
        val showPassword: Boolean = false,
        val rememberUser: Boolean = false
) : EmaBaseState