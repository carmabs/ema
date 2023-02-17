package com.carmabs.ema.presentation.ui.profile

import com.carmabs.ema.core.navigator.EmaDestination

sealed class ProfileDestination : EmaDestination() {
    object SampleDestination : ProfileDestination()
}
