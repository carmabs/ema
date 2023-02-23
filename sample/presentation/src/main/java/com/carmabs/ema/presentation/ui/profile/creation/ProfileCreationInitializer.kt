package com.carmabs.ema.presentation.ui.profile.creation

import com.carmabs.ema.core.initializer.EmaInitializer

sealed class ProfileCreationInitializer: EmaInitializer {
    object User : ProfileCreationInitializer()
    object Admin: ProfileCreationInitializer()
}