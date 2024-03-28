package com.carmabs.ema.presentation.dialog.simple

/**
 * Created by Carlos Mateo Benito on 22/3/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
sealed interface SimpleDialogAction {
    data object Cancel: SimpleDialogAction
    data object Accept: SimpleDialogAction
    data object BackPressed: SimpleDialogAction
}