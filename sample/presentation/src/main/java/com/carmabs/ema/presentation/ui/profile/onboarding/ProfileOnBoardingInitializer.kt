package com.carmabs.ema.presentation.ui.profile.onboarding

import com.carmabs.domain.model.User
import com.carmabs.ema.core.initializer.EmaInitializer

sealed class ProfileOnBoardingInitializer : EmaInitializer {
    data class Default(val admin: User): ProfileOnBoardingInitializer()
}