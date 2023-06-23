package com.carmabs.ema.core.navigator

import com.carmabs.ema.core.initializer.EmaInitializer

/**
 * Navigation state to set navigation destination. All clases where [EmaNavigator] can navigate
 * have to implement this interface
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

abstract class EmaDestination {
    var isNavigated: Boolean = false
        private set

    internal fun setNavigated() {
        isNavigated = true
    }

    internal fun resetNavigated() {
        isNavigated = false
    }
}