package com.carmabs.ema.presentation.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import com.carmabs.ema.android.navigation.EmaActivityNavControllerHost
import com.carmabs.ema.android.ui.EmaToolbarActivity
import com.carmabs.ema.android.viewmodel.EmaAndroidEmptyViewModel
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.core.navigator.EmaEmptyDestination
import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.core.state.EmaEmptyState
import com.carmabs.ema.core.viewmodel.EmaEmptyViewModel
import com.carmabs.ema.sample.ema.R
import com.carmabs.ema.sample.ema.databinding.SplashActivityBinding
import com.google.android.material.appbar.AppBarLayout


class SplashActivity :
    EmaToolbarActivity<SplashActivityBinding,EmaEmptyState, EmaEmptyViewModel, EmaEmptyDestination>() {

    override fun createViewBinding(inflater: LayoutInflater): SplashActivityBinding {
        return SplashActivityBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbar()

    }

    override fun SplashActivityBinding.provideToolbar(): Toolbar {
       return tbSplash
    }

    override fun SplashActivityBinding.provideToolbarLayout(): AppBarLayout {
       return ablSplash
    }

    override fun provideAndroidViewModel(): EmaAndroidViewModel {
        return EmaAndroidEmptyViewModel()
    }

    override fun SplashActivityBinding.onStateNormal(data: EmaEmptyState){
    
    }

    override val navigator: EmaNavigator<EmaEmptyDestination> = EmaActivityNavControllerHost(
       this,
       R.id.navHostFragment,
       R.navigation.main_graph
   )
}
