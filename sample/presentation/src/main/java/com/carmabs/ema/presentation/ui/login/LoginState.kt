package com.carmabs.ema.presentation.ui.login

import com.carmabs.ema.core.state.EmaDataState

data class LoginState(
    val userName: String,
    val userPassword:String,
):EmaDataState

