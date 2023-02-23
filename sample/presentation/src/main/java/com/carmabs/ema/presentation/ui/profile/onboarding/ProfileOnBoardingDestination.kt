package com.carmabs.ema.presentation.ui.profile.onboarding

import com.carmabs.ema.core.navigator.EmaDestination

sealed class ProfileOnBoardingDestination : EmaDestination() {
    class ProfileCreation : ProfileOnBoardingDestination()
}
