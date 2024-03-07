package com.carmabs.ema.presentation.ui.home

import com.carmabs.domain.model.Role
import com.carmabs.domain.model.User
import com.carmabs.ema.core.model.EmaText
import com.carmabs.ema.core.state.EmaDataState

data class HomeState(
    val userData: UserData,
    val userList: List<User>
) : EmaDataState {

    companion object {
        val DEFAULT = HomeState(
            userData = UserData.Basic,
            userList = emptyList()
        )
    }

    sealed class UserData(val role:Role) {
        data object Basic : UserData(Role.BASIC)

        data class Admin(val name: String) : UserData(Role.ADMIN)
    }

    val showCreateButton
        get() = userData is UserData.Admin

    val showFriendList
        get() = userData is UserData.Admin
}

