package com.carmabs.ema.presentation.ui.home

import androidx.fragment.app.Fragment
import com.carmabs.ema.android.navigation.EmaFragmentNavControllerNavigator
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.sample.ema.R

class HomeNavigator(
    fragment: Fragment
) : EmaFragmentNavControllerNavigator<HomeDestination>(fragment) {

    override fun navigate(destination: HomeDestination) {
        when(destination){
            is HomeDestination.Profile -> {
                navController.navigate(R.id.action_homeFragment_to_profileActivity,setInitializer(destination.initializer))
            }
        }
    }
}
