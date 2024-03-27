package com.carmabs.ema.presentation.ui.splash

import com.carmabs.ema.core.navigator.EmaNavigationEvent

sealed class SplashNavigationEvent : EmaNavigationEvent {
    data object SplashFinished : SplashNavigationEvent()
}
