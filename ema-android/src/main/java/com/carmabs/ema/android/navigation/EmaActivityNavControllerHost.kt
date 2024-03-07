package com.carmabs.ema.android.navigation

import android.app.Activity
import android.content.Intent
import com.carmabs.ema.android.constants.EMA_RESULT_CODE
import com.carmabs.ema.android.constants.EMA_RESULT_KEY
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.google.gson.Gson

/**
 * Created by Carlos Mateo Benito on 29/7/22.
 *
 * <p>
 * Copyright (c) 2022 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 *
 * Navigator to handle navigation through navController in an Activity.
 * It acts like a fragment host that navigates only through its child fragments.
 * Created by: Carlos Mateo Benito on 29/07/22.
 */
class EmaActivityNavControllerHost(
    activity: Activity,
    navHostId: Int,
    graphId: Int
) : EmaActivityNavControllerNavigator<EmaNavigationEvent.EMPTY>(activity, navHostId, graphId) {

    private val gson by lazy {
        Gson()
    }
    override fun navigate(navigationEvent: EmaNavigationEvent.EMPTY) = Unit

    /**
     * Navigates back
     * @return true if a fragment has been popped, false if backstack is empty, in that case, finish
     * the activity provided.
     */
    override fun navigateBack(result: Any?): Boolean {
        val hasMoreFragments = navController.popBackStack()
        if (!hasMoreFragments) {
            result?.also {
                activity.setResult(
                    EMA_RESULT_CODE,
                    Intent().putExtra(EMA_RESULT_KEY, gson.toJson(it))
                )
            }
            activity.finish()
        }
        return hasMoreFragments
    }
}