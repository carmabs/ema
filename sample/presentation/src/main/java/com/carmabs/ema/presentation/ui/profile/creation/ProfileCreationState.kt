package com.carmabs.ema.presentation.ui.profile.creation

import com.carmabs.domain.model.Role
import com.carmabs.ema.core.constants.STRING_EMPTY
import com.carmabs.ema.core.state.EmaDataState

data class ProfileCreationState(
    val role: Role = Role.BASIC,
    val name: String = STRING_EMPTY,
    val surname: String = STRING_EMPTY
) : EmaDataState {

    val roleText
        get() = role.name
}

