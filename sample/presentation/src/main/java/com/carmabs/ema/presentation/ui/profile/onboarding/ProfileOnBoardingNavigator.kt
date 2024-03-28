package com.carmabs.ema.presentation.ui.profile.onboarding

import androidx.activity.ComponentActivity
import androidx.navigation.NavController
import com.carmabs.domain.model.Role
import com.carmabs.ema.android.initializer.EmaInitializerBundle
import com.carmabs.ema.android.initializer.bundle.strategy.BundleSerializerStrategy
import com.carmabs.ema.android.initializer.bundle.strategy.KSerializationBundleStrategy
import com.carmabs.ema.compose.extension.navigate
import com.carmabs.ema.compose.extension.navigateBack
import com.carmabs.ema.compose.extension.routeId
import com.carmabs.ema.compose.navigation.EmaComposableNavigator
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationInitializer
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationNavigationEvent
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationScreenContent

class ProfileOnBoardingNavigator(
    activity: ComponentActivity,
    navController: NavController
) : EmaComposableNavigator(
    context = activity,
    navController = navController
) {
    fun handleProfileOnBoardingNavigation(navigationEvent: ProfileOnBoardingNavigationEvent) {
        when (navigationEvent) {
            is ProfileOnBoardingNavigationEvent.ProfileCreation -> {
                navController.navigate(
                    route = ProfileCreationScreenContent::class.routeId,
                    initializerBundle = EmaInitializerBundle(
                        mapToCreationInitializer(navigationEvent.role),
                        BundleSerializerStrategy.kSerialization(ProfileCreationInitializer.serializer())
                    )
                )
            }
        }

    }

    private fun mapToCreationInitializer(role: Role) = when (role) {
        Role.ADMIN -> ProfileCreationInitializer.Admin
        Role.BASIC -> ProfileCreationInitializer.UserBasic
    }

    fun handleProfileCreationNavigation(navigationEvent: ProfileCreationNavigationEvent) {
        when (navigationEvent) {
            ProfileCreationNavigationEvent.DialogConfirmationAccepted -> navController.navigateBack()
        }
    }
}
