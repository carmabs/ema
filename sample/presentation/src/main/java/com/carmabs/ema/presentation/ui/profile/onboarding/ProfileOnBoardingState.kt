package com.carmabs.ema.presentation.ui.profile.onboarding

import com.carmabs.domain.model.User
import com.carmabs.ema.core.constants.STRING_EMPTY
import com.carmabs.ema.core.state.EmaDataState

data class ProfileOnBoardingState(
    val user: User?
) : EmaDataState {

    companion object {
        val DEFAULT = ProfileOnBoardingState(
            user = null
        )
    }

    val userName
        get() = user?.let {
            "${it.name} ${it.surname}"
        } ?: STRING_EMPTY
}

