package com.carmabs.ema.core.action

/**
 * Created by Carlos Mateo Benito on 29/9/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
sealed interface ViewEmaAction : EmaAction {
    object Started : ViewEmaAction
    object Resumed : ViewEmaAction
    object Paused : ViewEmaAction
    object Stopped : ViewEmaAction

}