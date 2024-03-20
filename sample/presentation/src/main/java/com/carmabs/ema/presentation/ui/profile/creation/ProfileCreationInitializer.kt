package com.carmabs.ema.presentation.ui.profile.creation

import com.carmabs.ema.core.initializer.EmaInitializer
import kotlinx.serialization.Serializable

@Serializable
sealed class ProfileCreationInitializer: EmaInitializer {
    @Serializable
    data object User : ProfileCreationInitializer()
    @Serializable
    data object Admin: ProfileCreationInitializer()
}