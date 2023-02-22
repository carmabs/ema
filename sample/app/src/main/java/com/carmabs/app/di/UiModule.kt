package com.carmabs.app.di

import com.carmabs.ema.presentation.ui.home.HomeViewModel
import com.carmabs.ema.presentation.ui.login.LoginViewModel
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationViewModel
import com.carmabs.ema.presentation.ui.profile.onboarding.ProfileOnBoardingViewModel
import com.carmabs.ema.presentation.ui.splash.SplashViewModel
import org.koin.dsl.module

val uiModule = module {
    factory { SplashViewModel() }
    factory { LoginViewModel(get(),get()) }
    factory { HomeViewModel(get(),get()) }
    factory { ProfileOnBoardingViewModel() }
    factory { ProfileCreationViewModel(get()) }
}