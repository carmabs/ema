package com.carmabs.ema.presentation.dialog.error

/**
 * Created by Carlos Mateo Benito on 22/3/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
sealed interface ErrorDialogAction {
    data object Accept: ErrorDialogAction
    data object BackPressed: ErrorDialogAction
}