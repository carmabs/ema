package com.carmabs.ema.compose.extension

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.carmabs.ema.android.extension.getResourceId
import com.carmabs.ema.core.model.EmaImage

/**
 * Created by Carlos Mateo Benito on 13/2/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */

@Composable
fun EmaImage.toComposableImage(
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {
    val painter = toComposePainter()
    Image(
        painter =  painter,
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter
    )
}

@Composable
fun EmaImage.toComposePainter(): Painter {
    return  when (this) {
        is EmaImage.Id -> {
            androidx.compose.ui.res.painterResource(id = id)
        }

        is EmaImage.Uri -> {
            androidx.compose.ui.res.painterResource(id = uri.getResourceId(LocalContext.current))
        }
    }
}