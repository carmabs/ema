package com.carmabs.ema.presentation.ui.home

import com.carmabs.domain.model.Role
import com.carmabs.domain.model.User
import com.carmabs.ema.core.state.EmaDataState

data class HomeState(
    val userData: UserData?,
    val userList: List<User>
) : EmaDataState {

    companion object {
        val DEFAULT = HomeState(
            userData = null,
            userList = emptyList()
        )
    }

    sealed class UserData(val role: Role) {
        data object Basic : UserData(Role.BASIC)

        data class Admin(val name: String, val surname: String) : UserData(Role.ADMIN)
    }

    val showCreateButton
        get() = userData is UserData.Admin

    val showAdminList
        get() = userData != null
}

