package com.carmabs.ema.presentation.ui.home

import com.carmabs.ema.core.action.EmaAction

/**
 * Created by Carlos Mateo Benito on 6/3/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
sealed interface HomeAction : EmaAction.Screen {
    data object ProfileClicked: HomeAction
}