package com.carmabs.ema.presentation.ui.home

import com.carmabs.ema.core.navigator.EmaDestination

sealed class HomeDestination : EmaDestination() {
    class Profile : HomeDestination()
}
