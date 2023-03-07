package com.carmabs.ema.presentation.ui.splash

import android.view.LayoutInflater
import android.view.ViewGroup
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.ui.EmaFragment
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.core.state.EmaEmptyState
import com.carmabs.ema.sample.ema.databinding.SplashFragmentBinding


class SplashFragment :
    EmaFragment<SplashFragmentBinding,EmaEmptyState, SplashViewModel, SplashDestination>() {

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SplashFragmentBinding {
        return SplashFragmentBinding.inflate(inflater,container,false)
    }



    override fun provideAndroidViewModel(): EmaAndroidViewModel {
        return SplashAndroidViewModel(injectDirect())
    }

    override fun SplashFragmentBinding.onStateNormal(data: EmaEmptyState){
    
    }

    override val navigator: EmaNavigator<SplashDestination> = SplashNavigator(this)
}
