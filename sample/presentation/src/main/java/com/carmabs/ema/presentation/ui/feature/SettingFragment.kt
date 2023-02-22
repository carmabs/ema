package com.carmabs.ema.presentation.ui.feature

import android.view.LayoutInflater
import android.view.ViewGroup
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.ui.EmaFragment
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.sample.ema.databinding.SettingFragmentBinding


class SettingFragment :
    EmaFragment<SettingFragmentBinding,SettingState, SettingViewModel, SettingDestination>() {

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SettingFragmentBinding {
        return SettingFragmentBinding.inflate(inflater,container,false)
    }

    override fun provideAndroidViewModel(): EmaAndroidViewModel {
        return SettingAndroidViewModel(injectDirect())
    }

    override fun SettingFragmentBinding.onStateNormal(data: SettingState){


    }

    override val navigator: EmaNavigator<SettingDestination> = SettingNavigator(this)
}
