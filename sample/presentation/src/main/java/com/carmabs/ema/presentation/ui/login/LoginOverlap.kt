package com.carmabs.ema.presentation.ui.login

/**
 * Created by Carlos Mateo Benito on 22/3/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
sealed interface LoginOverlap {
    data object ErrorUserEmpty : LoginOverlap
    data object ErrorBadCredentials : LoginOverlap
    data object ErrorPasswordEmpty : LoginOverlap
}