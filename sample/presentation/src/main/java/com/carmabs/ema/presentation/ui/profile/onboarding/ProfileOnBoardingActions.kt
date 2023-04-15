package com.carmabs.ema.presentation.ui.profile.onboarding

import com.carmabs.ema.core.action.EmaAction

sealed interface ProfileOnBoardingActions : EmaAction {
    object AdminClicked: ProfileOnBoardingActions
    object UserClicked: ProfileOnBoardingActions
}