package com.carmabs.ema.android.navigation

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.carmabs.ema.core.navigator.EmaNavigationEvent

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
    private val fragment:Fragment
) : EmaNavControllerNavigator<D> {

    final override val activity: Activity by lazy {
        fragment.requireActivity()
    }

    final override val navController: NavController by lazy {
        fragment.findNavController()
    }

}