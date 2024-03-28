package com.carmabs.ema.compose.extension

import com.carmabs.ema.compose.ui.EmaComposableScreenContent
import kotlin.reflect.KClass


val <T : EmaComposableScreenContent<*, *>> KClass<T>.routeId: String
    get() = "screen/${this.java.name}"

