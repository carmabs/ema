package com.carmabs.ema.presentation.ui.login

import com.carmabs.ema.core.navigator.EmaDestination

sealed class LoginDestination : EmaDestination() {
    class Home : LoginDestination()
}