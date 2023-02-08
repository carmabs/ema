package com.carmabs.ema.presentation.ui.splash

import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.viewmodel.EmaViewModel
import kotlinx.coroutines.delay

class SplashViewModel: EmaViewModel<SplashState,SplashDestination>(){
	
	override suspend fun onCreateState(initializer: EmaInitializer?): SplashState {
        return SplashState()
    }

    override suspend fun onCreated() {
        super.onCreated()
        delay(5000)
        navigate(SplashDestination.Login)
    }
}
