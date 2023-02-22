package com.carmabs.ema.presentation.ui.feature

import com.carmabs.ema.core.navigator.EmaDestination

sealed class SettingDestination : EmaDestination() {
    object SampleDestination : SettingDestination()
}
