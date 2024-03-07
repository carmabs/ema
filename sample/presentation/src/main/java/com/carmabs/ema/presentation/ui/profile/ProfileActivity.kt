package com.carmabs.ema.presentation.ui.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.extension.getInitializer
import com.carmabs.ema.compose.extension.createComposableScreen
import com.carmabs.ema.compose.extension.routeId
import com.carmabs.ema.core.model.EmaBackHandlerStrategy
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationAction
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationScreenContent
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationViewModel
import com.carmabs.ema.presentation.ui.profile.onboarding.ProfileOnBoardingNavigator
import com.carmabs.ema.presentation.ui.profile.onboarding.ProfileOnBoardingScreenContent
import com.carmabs.ema.presentation.ui.profile.onboarding.ProfileOnBoardingViewModel


class ProfileActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()
            val navigator = remember {
                ProfileOnBoardingNavigator(this,navController)
            }
            NavHost(
                navController = navController,
                startDestination = ProfileOnBoardingScreenContent::class.routeId
            ) {
                createComposableScreen(
                    overrideInitializer = getInitializer(),
                    screenContent = ProfileOnBoardingScreenContent(),
                    onNavigationEvent = {
                        navigator.handleProfileOnBoardingNavigation(it)
                    },
                    viewModel = { injectDirect<ProfileOnBoardingViewModel>() }
                )
                createComposableScreen(
                    screenContent = ProfileCreationScreenContent(),
                    viewModel = { injectDirect<ProfileCreationViewModel>() },
                    onNavigationEvent = {


                    },
                    onBackEvent = { data,actions->
                        actions.dispatch(ProfileCreationAction.OnBack)
                        EmaBackHandlerStrategy.Cancelled
                    }
                )
            }
        }
    }
}
