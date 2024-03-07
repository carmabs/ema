package com.carmabs.ema.presentation.ui.home

import androidx.fragment.app.Fragment
import com.carmabs.ema.android.extension.navigate
import com.carmabs.ema.android.navigation.EmaFragmentNavControllerNavigator
import com.carmabs.ema.presentation.ui.profile.onboarding.ProfileOnBoardingInitializer
import com.carmabs.ema.sample.ema.R

class HomeNavigator(
    fragment: Fragment
) : EmaFragmentNavControllerNavigator<HomeNavigationEvent>(fragment) {

    override fun navigate(destination: HomeNavigationEvent) {
        when (destination) {
            is HomeNavigationEvent.ProfileClicked -> {
                navController.navigate(
                    id = R.id.action_homeFragment_to_profileActivity,
                    initializer = ProfileOnBoardingInitializer.Default(destination.user)
                )
            }
        }
    }
}
