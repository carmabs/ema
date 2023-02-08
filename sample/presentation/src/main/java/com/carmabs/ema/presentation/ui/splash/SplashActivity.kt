package com.carmabs.ema.presentation.ui.splash

import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import com.carmabs.ema.android.ui.EmaToolbarActivity
import com.google.android.material.appbar.AppBarLayout
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.sample.ema.R
import com.carmabs.ema.sample.ema.databinding.SplashActivityBinding


class SplashActivity :
    EmaToolbarActivity<SplashActivityBinding,SplashState, SplashViewModel, SplashDestination>() {

    override fun createViewBinding(inflater: LayoutInflater): SplashActivityBinding {
        return SplashActivityBinding.inflate(inflater)
    }
    
    override fun SplashActivityBinding.provideToolbar(): Toolbar {
       return tbSplash
    }

    override fun SplashActivityBinding.provideToolbarLayout(): AppBarLayout {
       return ablSplash
    }

    override fun provideAndroidViewModel(): EmaAndroidViewModel {
        return SplashAndroidViewModel(injectDirect())
    }

    override fun SplashActivityBinding.onStateNormal(data: SplashState){
    
    }

    override val navigator: EmaNavigator<SplashDestination> = SplashNavigator(
       this,
       R.id.navHostFragment,
       R.navigation.splash_graph
   )
}
