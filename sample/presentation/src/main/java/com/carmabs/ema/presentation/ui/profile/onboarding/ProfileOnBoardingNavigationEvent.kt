package com.carmabs.ema.presentation.ui.profile.onboarding

import com.carmabs.domain.model.Role
import com.carmabs.ema.core.navigator.EmaNavigationEvent

sealed class ProfileOnBoardingNavigationEvent : EmaNavigationEvent {
    data class  ProfileCreation(val role:Role) : ProfileOnBoardingNavigationEvent()
}
