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
sealed interface EmaNavigationDirection {

    data class Forward internal constructor(val navigationEvent: EmaNavigationEvent) : EmaNavigationDirection

    object Back : EmaNavigationDirection
}

inline fun EmaNavigationDirection.onForward(action: (EmaNavigationEvent) -> Unit) {
    if (this is EmaNavigationDirection.Forward) {
        action.invoke(navigationEvent)
    }
}

inline fun EmaNavigationDirection.onBack(action: () -> Unit) {
    if (this is EmaNavigationDirection.Back) {
        action.invoke()
    }
}