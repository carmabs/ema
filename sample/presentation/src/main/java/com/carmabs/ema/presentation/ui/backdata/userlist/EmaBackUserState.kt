package com.carmabs.ema.presentation.ui.backdata.userlist

import com.carmabs.ema.core.state.EmaBaseState

/**
 *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 *
 * Date: 2019-11-07
 */

data class EmaBackUserState(
        val listUsers: List<EmaBackUserModel> = listOf(),
        val noUserVisibility: Boolean = true
) : EmaBaseState