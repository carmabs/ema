package com.carmabs.ema.presentation.ui.home

import com.carmabs.domain.model.User
import com.carmabs.ema.core.initializer.EmaInitializer

sealed class HomeInitializer : EmaInitializer {
    data class Admin(val admin: User) : HomeInitializer()
    object BasicUser : HomeInitializer()
}