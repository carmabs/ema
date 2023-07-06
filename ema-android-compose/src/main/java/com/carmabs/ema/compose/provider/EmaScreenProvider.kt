package com.carmabs.ema.compose.provider

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.android.viewmodel.EmaViewModelFactory
import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaViewModel

class EmaScreenProvider {

    @Composable
    fun <S:EmaDataState,D:EmaDestination>provideComposableViewModel(androidViewModel:EmaAndroidViewModel<S,D>):EmaViewModel<S,D>{
        return viewModel(
            androidViewModel::class.java,
            factory = EmaViewModelFactory(androidViewModel)
        ).emaViewModel
    }
}