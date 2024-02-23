package com.carmabs.ema.compose.provider

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.android.viewmodel.EmaViewModelFactory
import com.carmabs.ema.core.extension.id
import com.carmabs.ema.core.extension.resultId
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaViewModel
import org.koin.androidx.compose.viewModel

object EmaScreenProvider {

    @Composable
    fun <S : EmaDataState, N : EmaNavigationEvent> provideComposableViewModel(viewModel: EmaViewModel<S, N>): EmaAndroidViewModel<S, N> {
        return viewModel(
            modelClass = EmaAndroidViewModel::class.java,
            key = viewModel::class.id(),
            factory = EmaViewModelFactory(viewModel)
        ) as EmaAndroidViewModel<S, N>
    }
}