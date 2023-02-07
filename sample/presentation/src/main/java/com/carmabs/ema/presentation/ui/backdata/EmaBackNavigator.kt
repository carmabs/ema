package com.carmabs.ema.presentation.ui.backdata

import android.app.Activity
import androidx.navigation.NavController
import com.carmabs.ema.R
import com.carmabs.ema.android.navigation.EmaAndroidNavigator
import com.carmabs.ema.core.navigator.EmaNavigationState
import com.carmabs.ema.core.navigator.EmaNavigator

/**
 *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 *
 * Date: 2019-11-07
 */

class EmaBackNavigator(
        override val navController: NavController,
        override val activity: Activity
) : EmaAndroidNavigator<EmaBackNavigator.Navigation> {

    sealed class Navigation : EmaNavigationState {

        object Result : Navigation() {
            override fun navigateWith(navigator: EmaNavigator<out EmaNavigationState>) {
                (navigator as? EmaBackNavigator)?.toResult()
            }
        }
    }

    private fun toResult() {
        navigateWithAction(R.id.action_emaBackFragment_to_emaBackResultFragment)
    }
}