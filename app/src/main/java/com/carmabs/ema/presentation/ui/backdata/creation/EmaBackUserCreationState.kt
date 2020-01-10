package com.carmabs.ema.presentation.ui.backdata.creation

import com.carmabs.ema.core.state.EmaBaseState
import com.carmabs.ema.presentation.STRING_EMPTY

/**
 *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 *
 * Date: 2019-11-07
 */

data class EmaBackUserCreationState (
        val name:String = STRING_EMPTY,
        val surname:String = STRING_EMPTY
) : EmaBaseState