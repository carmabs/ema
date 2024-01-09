package com.carmabs.ema.compose.extension

import android.util.DisplayMetrics
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.carmabs.ema.android.extension.screenHeightPx
import com.carmabs.ema.android.extension.screenWidthPx

/**
 * Created by Carlos Mateo Benito on 26/5/23
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 *
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
@Composable
fun screenHeightPx(): Float {
    val configuration = LocalConfiguration.current
    return configuration.screenHeightPx
}

@Composable
fun screenWidthPx(): Float {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthPx
}

@Composable
fun screenHeightDp(): Dp {
    val configuration = LocalConfiguration.current
    return configuration.screenHeightDp.dp
}

@Composable
fun screenWidthDp(): Dp {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp.dp
}

@Composable
fun Int.pxToDp(): Dp {
    val density = LocalDensity.current
    return with(density){
        toDp()
    }
}

@Composable
fun Float.pxToDp(): Dp {
    val screenPixelDensity = LocalContext.current.resources.displayMetrics.density
    return  (this / screenPixelDensity).dp
}

@Composable
fun Dp.toPx(): Float {
   return LocalDensity.current.run { toPx() }
}