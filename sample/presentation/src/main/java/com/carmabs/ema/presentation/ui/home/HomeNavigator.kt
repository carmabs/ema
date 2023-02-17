package com.carmabs.ema.presentation.ui.home

import androidx.fragment.app.Fragment
import com.carmabs.ema.android.navigation.EmaFragmentNavControllerNavigator
import com.carmabs.ema.sample.ema.R

class HomeNavigator(
    fragment: Fragment
) : EmaFragmentNavControllerNavigator<HomeDestination>(fragment) {

    override fun navigate(navigationTarget: HomeDestination) {
        when(navigationTarget){
            HomeDestination.Profile -> {
                navController.navigate(R.id.home)
            }
        }
    }
}
