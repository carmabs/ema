package com.carmabs.domain.usecase


import com.carmabs.domain.model.LoginRequest
import com.carmabs.domain.model.User
import com.carmabs.domain.repository.Repository


/**
 * Login
 *
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

class LoginUseCase(private val repository: Repository) {


    @Throws
    suspend fun doLogin(loginRequest: LoginRequest): User {
        return repository.login(loginRequest)
    }
}