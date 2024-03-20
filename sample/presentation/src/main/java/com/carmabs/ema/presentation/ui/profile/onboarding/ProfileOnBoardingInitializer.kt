package com.carmabs.ema.presentation.ui.profile.onboarding

import com.carmabs.ema.core.initializer.EmaInitializer
import kotlinx.serialization.Serializable


@Serializable
sealed class ProfileOnBoardingInitializer : EmaInitializer {

    @Serializable
    data class Default(val admin: String): ProfileOnBoardingInitializer()
}