package com.carmabs.ema.android.navigation

import android.app.Activity
import android.content.Intent
import androidx.navigation.NavController
import com.carmabs.ema.android.constants.EMA_RESULT_CODE
import com.carmabs.ema.android.constants.EMA_RESULT_KEY
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.navigator.EmaNavigator
import com.google.gson.Gson


class EmaEmptyNavigator constructor(
    private val activity: Activity,
    private val navController: NavController
) : EmaNavigator<EmaNavigationEvent.EMPTY> {

    private val gson by lazy {
        Gson()
    }
    override fun navigate(navigationEvent: EmaNavigationEvent.EMPTY) = Unit

    override fun navigateBack(result:Any?): Boolean {
        val hasMoreBackScreens = navController.popBackStack()
        if (!hasMoreBackScreens) {
            result?.also {
                    activity.setResult(
                        EMA_RESULT_CODE,
                        Intent().putExtra(EMA_RESULT_KEY, gson.toJson(it))
                    )
                }
                activity.finish()
        }

        return hasMoreBackScreens
    }
}