package com.carmabs.ema.android.compose.navigation

import android.app.Activity
import androidx.navigation.NavController
import com.carmabs.ema.core.navigator.EmaEmptyDestination
import com.carmabs.ema.core.navigator.EmaNavigator


class EmaComposableNavigatorEmpty(
    private val activity: Activity,
    private val navController: NavController
) : EmaNavigator<EmaEmptyDestination> {

    override fun navigate(destination: EmaEmptyDestination) = Unit

    override fun navigateBack(): Boolean {
        val hasMoreBackScreens = navController.popBackStack()
        if (!hasMoreBackScreens)
            activity.finish()

        return hasMoreBackScreens
    }
}