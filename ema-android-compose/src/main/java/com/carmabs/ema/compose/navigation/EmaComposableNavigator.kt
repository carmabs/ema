package com.carmabs.ema.compose.navigation

import android.app.Activity
import android.content.Context
import androidx.navigation.NavController
import com.carmabs.ema.android.extension.findActivity


abstract class EmaComposableNavigator(
    protected val context: Context,
    protected val navController: NavController
) {

    protected val activity: Activity = context.findActivity()
    fun navigateBack(closeActivityWhenBackstackIsEmpty: Boolean = true): Boolean {
        val hasMoreBackScreens = navController.popBackStack()
        if (!hasMoreBackScreens && closeActivityWhenBackstackIsEmpty)
            activity.finish()

        return hasMoreBackScreens
    }
}