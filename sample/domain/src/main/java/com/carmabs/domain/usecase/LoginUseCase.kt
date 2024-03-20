package com.carmabs.domain.usecase

import com.carmabs.domain.exception.LoginException
import com.carmabs.domain.model.LoginRequest
import com.carmabs.domain.model.User
import com.carmabs.domain.repository.Repository
import com.carmabs.ema.core.model.EmaResult
import com.carmabs.ema.core.usecase.EmaUseCase


/**
 * Login
 *
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

class LoginUseCase(private val repository: Repository) : EmaUseCase<LoginRequest, EmaResult<User,LoginException>>() {

    override suspend fun useCaseFunction(input: LoginRequest): EmaResult<User,LoginException> {
        return repository.login(input)
    }
}