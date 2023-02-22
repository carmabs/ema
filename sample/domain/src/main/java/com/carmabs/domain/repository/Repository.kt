package com.carmabs.domain.repository

import com.carmabs.domain.model.LoginRequest
import com.carmabs.domain.model.User

/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

interface Repository {

    suspend fun login(loginRequest: LoginRequest): User
    suspend fun getFriendsList(): List<User>
}