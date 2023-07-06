package com.carmabs.ema.compose.navigation

import android.app.Activity
import androidx.navigation.NavController
import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.navigator.EmaNavigator


abstract class EmaComposableNavigator<D : EmaDestination>(
    protected val activity: Activity,
    protected val navController: NavController,
    private val closeActivityWhenBackstackIsEmpty:Boolean = true
) : EmaNavigator<D> {

    override fun navigateBack(): Boolean {
        val hasMoreBackScreens = navController.popBackStack()
        if (!hasMoreBackScreens && closeActivityWhenBackstackIsEmpty)
            activity.finish()

        return hasMoreBackScreens
    }
}