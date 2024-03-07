package com.carmabs.app.di

import androidx.fragment.app.FragmentManager
import com.carmabs.ema.presentation.dialog.AppDialogProvider
import com.carmabs.ema.presentation.dialog.error.ErrorDialogProvider
import com.carmabs.ema.presentation.dialog.loading.LoadingDialogProvider
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogProvider
import com.carmabs.ema.presentation.ui.home.HomeState
import com.carmabs.ema.presentation.ui.home.HomeViewModel
import com.carmabs.ema.presentation.ui.login.LoginState
import com.carmabs.ema.presentation.ui.login.LoginViewModel
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationState
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationViewModel
import com.carmabs.ema.presentation.ui.profile.onboarding.ProfileOnBoardingState
import com.carmabs.ema.presentation.ui.profile.onboarding.ProfileOnBoardingViewModel
import com.carmabs.ema.presentation.ui.splash.SplashViewModel
import org.koin.dsl.module

val uiModule = module {

    factory { (fragmentManager: FragmentManager) ->
        AppDialogProvider(
            fragmentManager,
            SimpleDialogProvider(fragmentManager),
            LoadingDialogProvider(fragmentManager),
            ErrorDialogProvider(fragmentManager)
        )
    }

    factory { SplashViewModel() }
    factory { LoginViewModel(get(),get(), get()) }
    factory { HomeViewModel(get(), get()) }
    factory { ProfileOnBoardingViewModel(get()) }
    factory { ProfileCreationViewModel(get(),get()) }

    factory { LoginState.DEFAULT }
    factory { HomeState.DEFAULT }
    factory { ProfileOnBoardingState.DEFAULT }
    factory { ProfileCreationState.DEFAULT }
}