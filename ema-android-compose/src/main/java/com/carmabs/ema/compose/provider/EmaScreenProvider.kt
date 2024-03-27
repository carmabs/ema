package com.carmabs.ema.compose.provider

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.android.viewmodel.EmaViewModelFactory
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaViewModel

object EmaScreenProvider {

    @Composable
    fun <S : EmaDataState, N : EmaNavigationEvent> provideComposableViewModel(
        viewModel: EmaViewModel<S, N>,
        savedStateHandle: SavedStateHandle?
    ): EmaAndroidViewModel<S, N> {
        return viewModel(
            modelClass = EmaAndroidViewModel::class.java,
            key = viewModel.id,
            factory = EmaViewModelFactory(viewModel, savedStateHandle)
        ) as EmaAndroidViewModel<S, N>
    }
}