package com.carmabs.ema.presentation.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import com.carmabs.ema.android.navigation.EmaActivityNavControllerHost
import com.carmabs.ema.android.ui.EmaToolbarActivity
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaViewModel
import com.carmabs.ema.sample.ema.R
import com.carmabs.ema.sample.ema.databinding.SplashActivityBinding
import com.google.android.material.appbar.AppBarLayout


class SplashActivity :
    EmaToolbarActivity<SplashActivityBinding,EmaDataState.EMPTY, EmaViewModel.EMPTY, EmaNavigationEvent.EMPTY>() {

    override fun createViewBinding(inflater: LayoutInflater): SplashActivityBinding {
        return SplashActivityBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbar(animate = false)

    }

    override fun SplashActivityBinding.provideToolbar(): Toolbar {
       return tbSplash
    }

    override fun SplashActivityBinding.provideToolbarLayout(): AppBarLayout {
       return ablSplash
    }

    override fun provideViewModel(): EmaViewModel.EMPTY {
        return EmaViewModel.EMPTY
    }

    override fun SplashActivityBinding.onStateNormal(data: EmaDataState.EMPTY){
    
    }

    override val navigator: EmaNavigator<EmaNavigationEvent.EMPTY> = EmaActivityNavControllerHost(
       this,
       R.id.navHostFragment,
       R.navigation.main_graph
   )
}
