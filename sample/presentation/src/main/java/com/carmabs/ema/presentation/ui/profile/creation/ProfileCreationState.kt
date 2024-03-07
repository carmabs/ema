package com.carmabs.ema.presentation.ui.profile.creation

import com.carmabs.domain.model.Role
import com.carmabs.ema.core.constants.STRING_EMPTY
import com.carmabs.ema.core.state.EmaDataState

data class ProfileCreationState(
    val role: Role,
    val name: String,
    val surname: String
) : EmaDataState {

    companion object {
        val DEFAULT = ProfileCreationState(
            Role.BASIC,
            STRING_EMPTY,
            STRING_EMPTY
        )
    }

    val roleText
        get() = role.name
}

