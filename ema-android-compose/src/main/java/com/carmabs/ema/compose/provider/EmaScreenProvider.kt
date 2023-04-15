package com.carmabs.ema.compose.provider

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.android.viewmodel.EmaViewModelFactory
import com.carmabs.ema.core.viewmodel.EmaViewModel

class EmaScreenProvider {

    @Composable
    fun provideComposableViewModel(androidViewModel:EmaAndroidViewModel):EmaViewModel<*,*>{
        val vm = viewModel(
            androidViewModel::class.java,
            factory = EmaViewModelFactory(androidViewModel)
        ).emaViewModel

        return vm
    }
}