package com.carmabs.ema.presentation.ui.profile.creation

import com.carmabs.domain.model.Role

/**
 * Created by Carlos Mateo Benito on 23/3/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
sealed interface ProfileCreationOverlap {

    data object DialogBackConfirmation : ProfileCreationOverlap

    data class DialogUserCreated(val role: Role) : ProfileCreationOverlap
}