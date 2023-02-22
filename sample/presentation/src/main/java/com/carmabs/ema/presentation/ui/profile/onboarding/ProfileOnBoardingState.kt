package com.carmabs.ema.presentation.ui.profile.onboarding

import com.carmabs.domain.model.User
import com.carmabs.ema.core.state.EmaDataState

data class ProfileOnBoardingState(val user: User = User()): EmaDataState{

    val userName
    get() = "${user.name} ${user.surname}"
}

