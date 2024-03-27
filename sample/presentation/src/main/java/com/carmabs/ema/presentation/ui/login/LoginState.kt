package com.carmabs.ema.presentation.ui.login

import com.carmabs.ema.core.constants.STRING_EMPTY
import com.carmabs.ema.core.state.EmaDataState

data class LoginState(
    val userName: String,
    val userPassword: String
) : EmaDataState {

    companion object {
        val DEFAULT = LoginState(
            userName = STRING_EMPTY,
            userPassword = STRING_EMPTY
        )
    }
}

