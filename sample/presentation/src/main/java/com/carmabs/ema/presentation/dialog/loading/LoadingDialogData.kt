package com.carmabs.ema.presentation.dialog.loading

import com.carmabs.ema.core.dialog.EmaDialogData

/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

data class LoadingDialogData(
    val title: String = "",
    val message: String = "",
    override val proportionWidth: Float? = 7 / 10f,
    override val proportionHeight: Float? = null,
    override val isModal: Boolean = true
) : EmaDialogData