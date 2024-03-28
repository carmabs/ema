package com.carmabs.ema.compose.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode

/**
 * Created by Carlos Mateo Benito on 13/5/23
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
@Composable
fun skipForPreview(
    previewComposable: (@Composable () -> Unit)? = null,
    skipContent: @Composable () -> Unit
) {
    if (!LocalInspectionMode.current) {
        skipContent.invoke()
    } else {
        previewComposable?.invoke()
    }
}

@Composable
fun isInPreview()  = LocalInspectionMode.current