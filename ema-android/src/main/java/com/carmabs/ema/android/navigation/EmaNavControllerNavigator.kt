package com.carmabs.ema.android.navigation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.carmabs.ema.android.extension.setInitializer
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.navigator.EmaNavigator

/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 *
 * Navigator to handle navigation through navController with navigation back support and no target navigation
 * Created by: Carlos Mateo Benito on 20/1/19.
 */
interface EmaNavControllerNavigator<D : EmaDestination> : EmaNavigator<D> {

    val navController: NavController

    val activity: Activity


    /**
     * Set the initializer for the incoming fragment.
     * @param initializer for the incoming view
     */
    @Deprecated( "Use extension EmaInitializer.toBundle() instead", ReplaceWith(
        "initializer.toBundle()",
        "android.os.Bundle",
        "com.carmabs.ema.android.extension.toBundle"
    )
    )
    fun setInitializer(
        initializer: EmaInitializer
    ): Bundle =
        Bundle().setInitializer(initializer)

    /**
     * Navigate with android architecture components within action ID
     * @param actionID
     * @param data
     * @param navOptions
     */
    fun navigateWithAction(
        @IdRes actionID: Int,
        data: Bundle? = null,
        navOptions: NavOptions? = null,
        extras: FragmentNavigator.Extras? = null
    ) {
        navController.navigate(actionID, data, navOptions, extras)
    }


    /**
     * Navigate to new activity
     * @param destinationActivity is the activity class where you are going to navigate
     * @param initializer is the data you want to pass to next activity. It will be handled in viewmodel
     * @param finishMain if [activity] must be finished when [destinationActivity] is launched
     */
    fun navigateToActivity(
        destinationActivity: Class<out ComponentActivity>,
        initializer: EmaInitializer? = null,
        finishMain: Boolean = false
    ) {
        activity.startActivity(
            Intent(activity.applicationContext, destinationActivity).apply {
                initializer?.also {
                    putExtra(EmaInitializer.KEY,it)
                }
            },
        )
        if (finishMain) {
            activity.finish()
        }
    }


    /**
     * Navigate with android architecture components within navDirections safeargs
     * @param navDirections
     * @param navOptions
     */
    fun navigateWithDirections(navDirections: NavDirections, navOptions: NavOptions? = null) {
        navController.navigate(navDirections, navOptions)
    }

    /**
     * Navigate with android architecture components within navDirections safeargs
     * @param navDirections
     * @param extras
     */
    fun navigateWithDirections(navDirections: NavDirections, extras: Navigator.Extras) {
        navController.navigate(navDirections, extras)
    }


    /**
     * Navigates back
     * @return true if a fragment has been popped, false if backstack is empty, in that case, finish
     * the activity provided.
     */
    override fun navigateBack(): Boolean {
        val hasMoreFragments = navController.popBackStack()
        if (!hasMoreFragments)
            activity.finish()

        return hasMoreFragments
    }
}