package com.carmabs.ema.presentation.ui.login

import androidx.fragment.app.Fragment
import com.carmabs.ema.android.extension.navigate
import com.carmabs.ema.android.navigation.EmaFragmentNavControllerNavigator
import com.carmabs.ema.presentation.ui.home.HomeInitializer
import com.carmabs.ema.sample.ema.R

class LoginNavigator(
    fragment: Fragment
) : EmaFragmentNavControllerNavigator<LoginNavigationEvent>(fragment) {

    override fun navigate(navigationEvent: LoginNavigationEvent) {
        when(navigationEvent){
            is LoginNavigationEvent.LoginSuccess -> {
                navController.navigate(
                    id = R.id.action_loginFragment_to_homeFragment,
                    initializer = mapToHomeInitializer(navigationEvent)
                )
            }
        }
    }

    private fun mapToHomeInitializer(navigationEvent: LoginNavigationEvent.LoginSuccess): HomeInitializer {
        return navigationEvent.userType.let {
            when (it) {
                is LoginNavigationEvent.LoginSuccess.UserType.Admin -> HomeInitializer.Admin(it.user)
                LoginNavigationEvent.LoginSuccess.UserType.Basic -> HomeInitializer.BasicUser
            }
        }
    }
}
