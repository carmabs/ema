package com.carmabs.ema.presentation.dialog.simple

import com.carmabs.ema.core.dialog.EmaDialogData
import com.carmabs.ema.core.model.EmaText


/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

data class SimpleDialogData(
    val title: EmaText,
    val message: EmaText,
    val showCross: Boolean = true,
    val showCancel:Boolean = false,
    val image: Int? = null,
    override val proportionWidth: Float? = 7.5f / 10f,
    override val proportionHeight: Float? = null,
    override val isModal: Boolean = true
) : EmaDialogData
      