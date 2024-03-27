package com.carmabs.ema.presentation.ui.home

import com.carmabs.domain.model.User
import com.carmabs.ema.core.navigator.EmaNavigationEvent

sealed class HomeNavigationEvent : EmaNavigationEvent {
    data class ProfileClicked(val user: User): HomeNavigationEvent()
}
