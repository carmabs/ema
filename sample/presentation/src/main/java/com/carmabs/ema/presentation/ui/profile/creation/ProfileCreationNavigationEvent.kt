package com.carmabs.ema.presentation.ui.profile.creation

import com.carmabs.ema.core.navigator.EmaNavigationEvent

/**
 * Created by Carlos Mateo Benito on 6/3/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
sealed interface ProfileCreationNavigationEvent: EmaNavigationEvent {
    data object DialogConfirmationAccepted : ProfileCreationNavigationEvent
}