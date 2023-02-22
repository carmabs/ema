package com.carmabs.ema.presentation.ui.home

import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.presentation.ui.profile.onboarding.ProfileOnBoardingInitializer

sealed class HomeDestination : EmaDestination() {
    data class Profile(val initializer: ProfileOnBoardingInitializer) : HomeDestination()
}
