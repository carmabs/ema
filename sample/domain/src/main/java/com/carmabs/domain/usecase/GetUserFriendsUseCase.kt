package com.carmabs.domain.usecase

import com.carmabs.domain.model.User
import com.carmabs.domain.repository.Repository
import com.carmabs.ema.core.usecase.EmaUseCase


/**
 * Login
 *
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

class GetUserFriendsUseCase(private val repository: Repository) : EmaUseCase<GetUserFriendsUseCase.Input,List<User>>() {

    override suspend fun useCaseFunction(input: Input):List<User> {
        return repository.getFriendsList(input.user)
    }

    data class Input(
        val user:User
    )


}