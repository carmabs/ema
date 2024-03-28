package com.carmabs.ema.presentation.ui.splash

import androidx.fragment.app.Fragment
import com.carmabs.ema.android.navigation.EmaFragmentNavControllerNavigator
import com.carmabs.ema.sample.ema.R

class SplashNavigator(
    fragment: Fragment
) : EmaFragmentNavControllerNavigator<SplashNavigationEvent>(fragment) {

    override fun navigate(destination: SplashNavigationEvent) {
        when(destination){
            is SplashNavigationEvent.SplashFinished -> navController.navigate(R.id.action_splashFragment_to_loginFragment)
        }
        
    }
}
