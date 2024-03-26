package com.carmabs.data.repository

import com.carmabs.domain.exception.LoginException
import com.carmabs.domain.model.LoginRequest
import com.carmabs.domain.model.Role
import com.carmabs.domain.model.User
import com.carmabs.domain.repository.Repository
import com.carmabs.ema.core.model.EmaResult
import com.carmabs.ema.core.model.onSuccess
import kotlinx.coroutines.delay


/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 *
 * Created by: Carlos Mateo Benito on 19/1/19.
 */
class MockRepository : Repository {
    override suspend fun login(loginRequest: LoginRequest): EmaResult<User, LoginException> {
        delay(2000)
        return when {
            (loginRequest.name.equals("Admin", true) && loginRequest.password == "1234") ->
                EmaResult.success(User("Carmabs", "Ema Creator", Role.ADMIN))

            (loginRequest.name.equals("User", true) && loginRequest.password == "1234") -> {
                EmaResult.success(User("Anonymous", "Testing", Role.BASIC))
            }

            else -> {
                EmaResult.failure(LoginException())
            }
        }
    }

    override suspend fun getFriendsList(user: User): List<User> {
        val userList = mutableListOf<User>()
        if (user.role == Role.BASIC)
            repeat(10) {
                userList.add(User("User$it", "Surname$it", Role.BASIC))
            }
        return userList
    }
}