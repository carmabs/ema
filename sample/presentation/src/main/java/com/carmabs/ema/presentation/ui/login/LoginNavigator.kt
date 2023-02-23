package com.carmabs.ema.presentation.ui.login

import androidx.fragment.app.Fragment
import com.carmabs.ema.android.extension.navigate
import com.carmabs.ema.android.navigation.EmaFragmentNavControllerNavigator
import com.carmabs.ema.sample.ema.R

class LoginNavigator(
    fragment: Fragment
) : EmaFragmentNavControllerNavigator<LoginDestination>(fragment) {

    override fun navigate(destination: LoginDestination) {
        when(destination){
            is LoginDestination.Home -> {
                navController.navigate(R.id.action_loginFragment_to_homeFragment,destination.initializer)
            }
        }
    }
}
