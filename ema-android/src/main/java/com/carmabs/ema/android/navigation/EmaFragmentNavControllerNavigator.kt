package com.carmabs.ema.android.navigation

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
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
 *  Navigator to handle navigation through navController in a Fragment
 * Created by: Carlos Mateo Benito on 29/07/22.
 */
abstract class EmaFragmentNavControllerNavigator<D : EmaNavigationEvent>(
    private val fragment: Fragment
) : EmaNavControllerNavigator<D> {

    private val gson by lazy {
        Gson()
    }

    final override val activity: Activity by lazy {
        fragment.requireActivity()
    }

    final override val navController: NavController by lazy {
        fragment.findNavController()
    }

    /**
     * Navigates back
     * @return true if a fragment has been popped, false if backstack is empty, in that case, finish
     * the activity provided.
     */
    override fun navigateBack(result: Any?): Boolean {
        val hasMoreFragments = navController.popBackStack()
        result?.also {
            fragment.setFragmentResult(
                EMA_RESULT_KEY, Bundle().apply {
                    putSerializable(EMA_RESULT_KEY, gson.toJson(it))
                }
            )
        }
        if (!hasMoreFragments)
            activity.finish()

        return hasMoreFragments
    }
}