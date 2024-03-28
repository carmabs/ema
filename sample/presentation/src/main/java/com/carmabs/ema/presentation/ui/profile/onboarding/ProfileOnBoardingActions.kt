package com.carmabs.ema.presentation.ui.profile.onboarding

import com.carmabs.ema.core.action.EmaAction

sealed interface ProfileOnBoardingActions : EmaAction.Screen {
    data object AdminClicked: ProfileOnBoardingActions
    data object UserClicked: ProfileOnBoardingActions
}