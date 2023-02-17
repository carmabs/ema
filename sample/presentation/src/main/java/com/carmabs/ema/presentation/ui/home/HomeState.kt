package com.carmabs.ema.presentation.ui.home

import com.carmabs.domain.model.User
import com.carmabs.ema.core.state.EmaDataState

data class HomeState(val userList:List<User>): EmaDataState

