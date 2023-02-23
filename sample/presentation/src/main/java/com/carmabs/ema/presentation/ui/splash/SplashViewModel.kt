package com.carmabs.ema.presentation.ui.splash

import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.state.EmaEmptyState
import com.carmabs.ema.presentation.base.BaseViewModel
import kotlinx.coroutines.delay

class SplashViewModel: BaseViewModel<EmaEmptyState,SplashDestination>(){
	
	override suspend fun onCreateState(initializer: EmaInitializer?): EmaEmptyState {
        return EmaEmptyState
    }

    override suspend fun onStateCreated() {
        super.onStateCreated()
        delay(1500)
        navigate(SplashDestination.Login())
    }
}
