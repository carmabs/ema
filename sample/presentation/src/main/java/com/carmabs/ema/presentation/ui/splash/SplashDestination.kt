package com.carmabs.ema.presentation.ui.splash

import com.carmabs.ema.core.navigator.EmaDestination

sealed class SplashDestination : EmaDestination() {
    class Login : SplashDestination()
}
