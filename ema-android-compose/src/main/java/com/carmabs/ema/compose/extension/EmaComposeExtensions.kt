package com.carmabs.ema.compose.extension

import com.carmabs.ema.compose.ui.EmaComposableScreenContent
import kotlin.reflect.KClass


fun <T : EmaComposableScreenContent<*, *>> KClass<T>.routeId(): String {
    return "screen/${this.java.name}/${this.hashCode()}"
}

