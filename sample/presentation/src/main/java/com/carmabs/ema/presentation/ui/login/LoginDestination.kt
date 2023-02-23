package com.carmabs.ema.presentation.ui.login

import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.presentation.ui.home.HomeInitializer

sealed class LoginDestination : EmaDestination() {
    data class Home(val initializer:HomeInitializer) : LoginDestination()
}
