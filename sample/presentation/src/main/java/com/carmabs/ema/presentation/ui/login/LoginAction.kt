package com.carmabs.ema.presentation.ui.login

import com.carmabs.ema.core.action.EmaAction

/**
 * Created by Carlos Mateo Benito on 7/3/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
sealed interface LoginAction : EmaAction.Screen {

    data object Login : LoginAction

    data object DeleteUser : LoginAction

    data class UserNameWritten(val user: String) : LoginAction

    data class PasswordWritten(val password: String) : LoginAction

    sealed interface Error : LoginAction {
        data object BadCredentialsAccepted : Error
        data object UserEmptyAccepted : Error
        data object PasswordEmptyAccepted : Error
        data object BackPressed : Error
    }
}