package com.carmabs.ema.presentation.ui.profile

import android.app.Activity
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import com.carmabs.ema.android.navigation.EmaActivityNavControllerNavigator

class ProfileNavigator(
    activity: Activity,
    @IdRes navHostId: Int,
    @NavigationRes graphId: Int
) : EmaActivityNavControllerNavigator<ProfileDestination>(
    activity = activity,
    navHostId = navHostId,
    graphId = graphId
) {

    override fun navigate(navigationTarget: ProfileDestination){
    
    }
}
