package com.carmabs.ema.android.extension

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.carmabs.ema.core.initializer.EmaInitializer

fun NavController.navigate(
    id: Int,
    initializer: EmaInitializer?,
    navOptions: NavOptions? = null,
    navExtras: Navigator.Extras? = null
) {
    navigate(id, initializer.toBundle(), navOptions, navExtras)
}

