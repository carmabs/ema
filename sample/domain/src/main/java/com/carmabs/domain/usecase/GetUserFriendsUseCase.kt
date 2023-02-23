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

class GetUserFriendsUseCase(private val repository: Repository) : EmaUseCase<Unit,List<User>>() {

    override suspend fun useCaseFunction(input: Unit):List<User> {
        return repository.getFriendsList()
    }


}