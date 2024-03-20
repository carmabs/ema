package com.carmabs.domain.repository

import com.carmabs.domain.exception.LoginException
import com.carmabs.domain.model.LoginRequest
import com.carmabs.domain.model.User
import com.carmabs.ema.core.model.EmaResult

/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

interface Repository {

    suspend fun login(loginRequest: LoginRequest): EmaResult<User,LoginException>
    suspend fun getFriendsList(): List<User>
}