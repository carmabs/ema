package com.carmabs.ema.presentation.ui.login

import com.carmabs.domain.model.User

/**
 * Created by Carlos Mateo Benito on 23/3/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
sealed interface LoginSingleEvent {
    data class Message(val userName: String) : LoginSingleEvent
    data class LastUserAdded(val user: User) : LoginSingleEvent
}