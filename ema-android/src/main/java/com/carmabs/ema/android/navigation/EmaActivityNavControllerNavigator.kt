package com.carmabs.ema.android.navigation

import android.app.Activity
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.carmabs.ema.android.extension.toBundle
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.navigator.EmaDestination

/**
 * Created by Carlos Mateo Benito on 29/7/22.
 *
 * <p>
 * Copyright (c) 2022 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 *
 * Navigator to handle navigation through navController in an Activity
 * Created by: Carlos Mateo Benito on 29/07/22.
 */
abstract class EmaActivityNavControllerNavigator<D : EmaDestination>(
    override val activity: Activity,
    @IdRes private val navHostId: Int,
    @NavigationRes private val graphId: Int
) : EmaNavControllerNavigator<D> {

    internal fun setup(initializer: EmaInitializer?) {
        navController.setGraph(graphId, initializer?.let {
            it.toBundle()
        } ?: activity.intent.extras)
    }

    final override val navController: NavController
        get() = (activity as? FragmentActivity)?.supportFragmentManager?.findFragmentById(navHostId)
            ?.findNavController() ?: activity.findNavController(navHostId)
}