package com.carmabs.ema.android.extension

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.carmabs.ema.android.initializer.EmaInitializerBundle

fun NavController.navigate(
    id: Int,
    initializerBundle: EmaInitializerBundle?,
    navOptions: NavOptions? = null,
    navExtras: Navigator.Extras? = null
) {
    navigate(id, initializerBundle.toBundle(), navOptions, navExtras)
}

