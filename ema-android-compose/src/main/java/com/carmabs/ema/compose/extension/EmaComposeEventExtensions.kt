package com.carmabs.ema.compose.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composer
import androidx.compose.runtime.LaunchedEffect
import com.carmabs.ema.core.model.EmaEvent
import com.carmabs.ema.core.model.onLaunched
import com.carmabs.ema.core.state.EmaExtraData

/**
 * Created by Carlos Mateo Benito on 20/9/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */

@Composable
fun <T>EmaEvent.toEventEffect(
    onLaunchedAction: suspend (EmaExtraData) -> T,
    onConsumedAction: suspend (T) -> Unit
) {
    EventEffect(this,onLaunchedAction,onConsumedAction)
}

@Composable
fun <T>EventEffect(
    emaEvent: EmaEvent,
    onLaunchedAction: suspend (EmaExtraData) -> T,
    onConsumedAction: suspend (T) -> Unit
) {
    emaEvent.onLaunched {
        LaunchedEffect(key1 = emaEvent) {
            val value = onLaunchedAction.invoke(it)
            onConsumedAction(value)

        }
    }
}