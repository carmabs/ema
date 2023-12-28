package com.carmabs.ema.core.navigator

/**
 * Created by Carlos Mateo Benito on 17/9/23.
 *
 * <p>
 * Copyright (c) 2022 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
sealed interface EmaNavigationDirectionEvent {
    data class Launched internal constructor(val navigationDirection: EmaNavigationDirection) : EmaNavigationDirectionEvent

    object OnNavigated : EmaNavigationDirectionEvent

    object None: EmaNavigationDirectionEvent

    fun hasNavigated(): Boolean {
        return this is OnNavigated
    }
}
inline fun EmaNavigationDirectionEvent.onNavigation(action: (navigationDirection:EmaNavigationDirection) -> Unit) {
    if (this is EmaNavigationDirectionEvent.Launched) {
        action(navigationDirection)
    }
}