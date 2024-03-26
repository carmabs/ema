package com.carmabs.ema.presentation.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.extension.getInitializer
import com.carmabs.ema.android.initializer.bundle.strategy.BundleSerializerStrategy
import com.carmabs.ema.android.initializer.savestate.emaSaveStateHandler
import com.carmabs.ema.compose.extension.asActionDispatcher
import com.carmabs.ema.compose.extension.createComposableScreen
import com.carmabs.ema.compose.extension.routeId
import com.carmabs.ema.compose.initializer.EmaInitializerSupport
import com.carmabs.ema.core.extension.checkNull
import com.carmabs.ema.core.model.EmaBackHandlerStrategy
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationAction
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationInitializer
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationScreenContent
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationViewModel
import com.carmabs.ema.presentation.ui.profile.onboarding.ProfileOnBoardingInitializer
import com.carmabs.ema.presentation.ui.profile.onboarding.ProfileOnBoardingNavigator
import com.carmabs.ema.presentation.ui.profile.onboarding.ProfileOnBoardingScreenContent
import com.carmabs.ema.presentation.ui.profile.onboarding.ProfileOnBoardingViewModel
import kotlinx.coroutines.launch


class ProfileActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val navigator = remember {
                ProfileOnBoardingNavigator(this, navController)
            }
            NavHost(
                navController = navController,
                startDestination = ProfileOnBoardingScreenContent::class.routeId
            ) {
                createComposableScreen(
                    initializerSupport = EmaInitializerSupport.kSerialization(
                        ProfileOnBoardingInitializer.serializer(),
                        getInitializer(BundleSerializerStrategy.kSerialization(ProfileOnBoardingInitializer.serializer()),savedInstanceState),
                    ),
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
                        navigator.handleProfileCreationNavigation(it)
                    },
                    onBackEvent = { data, actions ->
                        actions.dispatch(ProfileCreationAction.OnBack)
                        EmaBackHandlerStrategy.Cancelled
                    },
                    initializerSupport = EmaInitializerSupport.kSerialization(
                        ProfileCreationInitializer.serializer()
                    ),
                    saveStateHandler = emaSaveStateHandler { coroutineScope, savedStateHandle, emaViewModel ->
                        Log.d("SAVE GET",savedStateHandle.keys().toString())
                        savedStateHandle.get<String>("A")?.also {
                            emaViewModel.asActionDispatcher<ProfileCreationAction>().dispatch(ProfileCreationAction.UserNameWritten(it))
                        }
                        coroutineScope.launch {
                            emaViewModel.subscribeStateUpdates().collect{
                                Log.d("SAVE READ",savedStateHandle.get<String>("A").checkNull())
                                Log.d("SAVE READ",savedStateHandle.get<String>("B").checkNull())
                                savedStateHandle["A"] = it.data.name
                                savedStateHandle["B"] = it.data.surname
                                Log.d("SAVE PUT",savedStateHandle.keys().toString())

                            }
                        }
                    }
                )
            }
        }
    }
}
