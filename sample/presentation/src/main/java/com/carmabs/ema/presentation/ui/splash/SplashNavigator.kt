package com.carmabs.ema.presentation.ui.splash

import android.app.Activity
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import com.carmabs.ema.android.navigation.EmaActivityNavControllerNavigator

class SplashNavigator(
    activity: Activity,
    @IdRes navHostId: Int,
    @NavigationRes graphId: Int
) : EmaActivityNavControllerNavigator<SplashDestination>(
    activity = activity,
    navHostId = navHostId,
    graphId = graphId
) {

    override fun navigate(navigationTarget: SplashDestination){
    
    }
}
