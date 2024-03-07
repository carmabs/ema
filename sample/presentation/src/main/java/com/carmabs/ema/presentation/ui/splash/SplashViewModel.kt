package com.carmabs.ema.presentation.ui.splash

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.presentation.base.BaseViewModel
import kotlinx.coroutines.delay

class SplashViewModel: BaseViewModel<EmaDataState.EMPTY,EmaAction.Screen.EMPTY,SplashNavigationEvent>(EmaDataState.EMPTY){
    override fun onAction(action: EmaAction.Screen.EMPTY) = Unit

    override fun onStateCreated(initializer: EmaInitializer?) {
        sideEffect {
            delay(1500)
            navigate(SplashNavigationEvent.SplashFinished)
        }

    }


}
