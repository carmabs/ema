package com.carmabs.ema.compose.extension

import android.content.res.Configuration

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
val Configuration.screenHeightPx
    get() = run { screenHeightDp.toFloat() * densityDpi / 160f }

val Configuration.screenWidthPx
    get() = run { screenWidthDp.toFloat() * densityDpi / 160f }