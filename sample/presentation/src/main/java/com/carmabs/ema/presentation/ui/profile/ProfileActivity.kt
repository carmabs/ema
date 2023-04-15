package com.carmabs.ema.presentation.ui.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.carmabs.ema.android.extension.getInitializer
import com.carmabs.ema.compose.extension.createComposableScreen
import com.carmabs.ema.compose.extension.injectDirectRemembered
import com.carmabs.ema.compose.extension.routeId
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationAndroidViewModel
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationScreenContent
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationState
import com.carmabs.ema.presentation.ui.profile.onboarding.ProfileOnBoardingAndroidViewModel
import com.carmabs.ema.presentation.ui.profile.onboarding.ProfileOnBoardingNavigator
import com.carmabs.ema.presentation.ui.profile.onboarding.ProfileOnBoardingScreenContent
import com.carmabs.ema.presentation.ui.profile.onboarding.ProfileOnBoardingState


class ProfileActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = ProfileOnBoardingScreenContent::class.routeId()
            ) {
                createComposableScreen(
                    overrideInitializer = getInitializer(),
                    defaultState = ProfileOnBoardingState(),
                    navigator = {
                        ProfileOnBoardingNavigator(
                            this@ProfileActivity,
                            navController,
                            it
                        )
                    },
                    screenContent = ProfileOnBoardingScreenContent(),
                    androidViewModel = { ProfileOnBoardingAndroidViewModel(injectDirectRemembered()) }
                )
                createComposableScreen(
                    defaultState = ProfileCreationState(),
                    screenContent = ProfileCreationScreenContent(),
                    androidViewModel = { ProfileCreationAndroidViewModel(injectDirectRemembered()) },
                    navController = navController
                )
            }
        }
    }
}
