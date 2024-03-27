package com.carmabs.ema.presentation.ui.home

import androidx.fragment.app.Fragment
import com.carmabs.ema.android.extension.navigate
import com.carmabs.ema.android.initializer.EmaInitializerBundle
import com.carmabs.ema.android.initializer.bundle.strategy.BundleSerializerStrategy
import com.carmabs.ema.android.initializer.bundle.strategy.KSerializationBundleStrategy
import com.carmabs.ema.android.navigation.EmaFragmentNavControllerNavigator
import com.carmabs.ema.presentation.ui.profile.onboarding.ProfileOnBoardingInitializer
import com.carmabs.ema.sample.ema.R

class HomeNavigator(
    fragment: Fragment
) : EmaFragmentNavControllerNavigator<HomeNavigationEvent>(fragment) {

    override fun navigate(navigationEvent: HomeNavigationEvent) {
        when (navigationEvent) {
            is HomeNavigationEvent.ProfileClicked -> {
                navController.navigate(
                    id = R.id.action_homeFragment_to_profileActivity,
                    initializerBundle = EmaInitializerBundle(
                        ProfileOnBoardingInitializer.Default(navigationEvent.user.name),
                        BundleSerializerStrategy.kSerialization(ProfileOnBoardingInitializer.serializer())
                    )
                )
            }
        }
    }
}
