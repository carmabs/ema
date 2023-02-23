package com.carmabs.ema.presentation.ui.profile.onboarding

import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationInitializer

sealed class ProfileOnBoardingDestination : EmaDestination() {
    data class ProfileCreation(val initializer: ProfileCreationInitializer) : ProfileOnBoardingDestination()
}
