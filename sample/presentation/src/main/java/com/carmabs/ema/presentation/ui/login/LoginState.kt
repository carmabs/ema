package com.carmabs.ema.presentation.ui.login

import com.carmabs.ema.core.constants.STRING_EMPTY
import com.carmabs.ema.core.state.EmaDataState

data class LoginState(
    val userName: String = STRING_EMPTY,
    val userPassword:String = STRING_EMPTY
):EmaDataState

