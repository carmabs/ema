package com.carmabs.app.di

import com.carmabs.ema.presentation.ui.login.LoginViewModel
import com.carmabs.ema.presentation.ui.splash.SplashViewModel
import org.koin.dsl.module

val uiModule = module {
    factory { SplashViewModel() }
    factory { LoginViewModel(get(),get()) }
}