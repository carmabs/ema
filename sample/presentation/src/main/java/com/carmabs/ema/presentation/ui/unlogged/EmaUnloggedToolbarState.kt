package com.carmabs.ema.presentation.ui.unlogged

import com.carmabs.ema.core.state.EmaBaseState

/**
 * State for error toolbar
 *
*
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

data class EmaUnloggedToolbarState(
        val visibility:Boolean = true
) : EmaBaseState