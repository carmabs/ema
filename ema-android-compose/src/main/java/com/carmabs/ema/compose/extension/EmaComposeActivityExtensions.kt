package com.carmabs.ema.compose.extension

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

/**
 * Created by Carlos Mateo Benito on 12/5/23
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
fun Context.findComponentActivity(): ComponentActivity {
    var context = this
    while (context is ContextWrapper) {
        if (context is ComponentActivity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}