package com.carmabs.ema.core.state

import com.carmabs.ema.core.dialog.EmaDialogData
import com.carmabs.ema.core.dialog.EmaDialogListener
import com.carmabs.ema.core.state.EmaDataState

/**
 * Class to handle extra data used for dialogs.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
data class EmaExtraDialogData(
    val data:EmaDialogData,
    val listener:EmaDialogListener?=null
):EmaDataState