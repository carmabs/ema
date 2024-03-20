package com.carmabs.ema.presentation.ui.home

import com.carmabs.domain.model.User
import com.carmabs.ema.core.initializer.EmaInitializer
import kotlinx.serialization.Serializable





@Serializable
sealed interface HomeInitializer : EmaInitializer {

    @Serializable
    data class Admin(val admin: User) : HomeInitializer

    @Serializable
    data object BasicUser : HomeInitializer
}