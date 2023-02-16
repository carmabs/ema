package com.carmabs.ema.presentation.ui.splash

import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.state.EmaEmptyState
import com.carmabs.ema.core.viewmodel.EmaViewModel
import kotlinx.coroutines.delay

class SplashViewModel: EmaViewModel<EmaEmptyState,SplashDestination>(){
	
	override suspend fun onCreateState(initializer: EmaInitializer?): EmaEmptyState {
        return EmaEmptyState()
    }

    override suspend fun onCreated() {
        super.onCreated()
        delay(5000)
        navigate(SplashDestination.Login)
    }
}
