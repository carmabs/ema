package com.carmabs.ema.presentation.ui.home

import com.carmabs.domain.model.Role
import com.carmabs.domain.model.User
import com.carmabs.ema.core.model.EmaText
import com.carmabs.ema.core.state.EmaDataState

data class HomeState(
    private val userRole: Role,
    val listName: EmaText,
    val userList: List<User>
) : EmaDataState {

    val showCreateButton
        get() = userRole == Role.ADMIN

    val showFriendList
        get() = userRole == Role.BASIC
}

