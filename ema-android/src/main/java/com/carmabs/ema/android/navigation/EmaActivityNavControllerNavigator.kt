package com.carmabs.ema.android.navigation

import android.app.Activity
import android.content.Intent
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.carmabs.ema.android.constants.EMA_RESULT_CODE
import com.carmabs.ema.android.constants.EMA_RESULT_KEY
import com.carmabs.ema.android.extension.toBundle
import com.carmabs.ema.android.initializer.bundle.strategy.BundleSerializerStrategy
import com.carmabs.ema.core.initializer.EmaInitializer
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
 * Navigator to handle navigation through navController in an Activity
 * Created by: Carlos Mateo Benito on 29/07/22.
 */
abstract class EmaActivityNavControllerNavigator<D : EmaNavigationEvent>(
    override val activity: Activity,
    @IdRes private val navHostId: Int,
    @NavigationRes private val graphId: Int
) : EmaNavControllerNavigator<D> {

    private val gson by lazy {
        Gson()
    }
    internal fun setup(initializer: EmaInitializer?,serializerStrategy: BundleSerializerStrategy) {
        navController.setGraph(graphId, initializer?.let {
            it.toBundle(serializerStrategy)
        } ?: activity.intent.extras)
    }

    final override val navController: NavController
        get() = (activity as? FragmentActivity)?.supportFragmentManager?.findFragmentById(navHostId)
            ?.findNavController() ?: activity.findNavController(navHostId)

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