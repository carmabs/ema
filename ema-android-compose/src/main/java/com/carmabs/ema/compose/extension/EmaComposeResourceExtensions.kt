package com.carmabs.ema.compose.extension

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

/**
 * Created by Carlos Mateo Benito on 11/5/23
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 * */
@Composable
fun @receiver:StringRes Int.toComposeString(): String {
    return stringResource(this)
}

@Composable
fun @receiver:StringRes Int.toComposeString(vararg args: Any): String {
    return if (args.isNotEmpty())
        String.format(stringResource(this), *args)
    else
        stringResource(this)
}

@Composable
fun @receiver:DrawableRes Int.toComposePainter(): Painter {
    return painterResource(id = this)
}

@Composable
fun @receiver:ColorRes Int.toComposeColor(): Color {
    return colorResource(id = this)
}