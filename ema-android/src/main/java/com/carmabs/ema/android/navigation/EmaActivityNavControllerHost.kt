package com.carmabs.ema.android.navigation

import android.app.Activity
import com.carmabs.ema.core.navigator.EmaEmptyNavigationEvent

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
) : EmaActivityNavControllerNavigator<EmaEmptyNavigationEvent>(activity, navHostId, graphId) {
    override fun navigate(destination: EmaEmptyNavigationEvent) = false
}