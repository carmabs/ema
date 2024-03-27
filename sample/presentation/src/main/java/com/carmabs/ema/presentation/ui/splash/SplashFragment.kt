package com.carmabs.ema.presentation.ui.splash

import android.view.LayoutInflater
import android.view.ViewGroup
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.ui.EmaFragment
import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.sample.ema.databinding.SplashFragmentBinding


class SplashFragment :
    EmaFragment<SplashFragmentBinding,EmaDataState.EMPTY, SplashViewModel, SplashNavigationEvent>() {

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SplashFragmentBinding {
        return SplashFragmentBinding.inflate(inflater,container,false)
    }



    override fun provideViewModel(): SplashViewModel {
        return injectDirect()
    }

    override fun SplashFragmentBinding.onStateNormal(data: EmaDataState.EMPTY){
    
    }

    override val navigator: EmaNavigator<SplashNavigationEvent> = SplashNavigator(this)
}
