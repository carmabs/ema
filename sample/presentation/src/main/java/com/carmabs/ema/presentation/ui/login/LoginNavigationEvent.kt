package com.carmabs.ema.presentation.ui.login

import com.carmabs.domain.model.User
import com.carmabs.ema.core.navigator.EmaNavigationEvent

sealed class LoginNavigationEvent : EmaNavigationEvent {
    data class LoginSuccess(val userType: UserType) : LoginNavigationEvent() {
        sealed interface UserType {
            data object Basic : UserType
            data class Admin(val user: User) : UserType
        }
    }
}
