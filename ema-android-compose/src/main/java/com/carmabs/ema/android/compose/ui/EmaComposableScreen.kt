package com.carmabs.ema.android.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carmabs.ema.android.compose.action.EmaComposableScreenActions
import com.carmabs.ema.android.compose.action.EmaComposableScreenActionsEmpty
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.android.viewmodel.EmaViewModelFactory
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.core.viewmodel.EmaViewModel

@Composable
fun <S : EmaDataState, VM : EmaViewModel<S, D>, D : EmaDestination,A: EmaComposableScreenActions> EmaComposableScreen(
    defaultState:S,
    androidViewModel: EmaAndroidViewModel,
    initializer: EmaInitializer? = null,
    navigator: EmaNavigator<D>? = null,
    screenActions:A,
    screenView: EmaComposableScreenView<S, VM, D,A>
) {
    val vm = viewModel(androidViewModel::class.java, factory = EmaViewModelFactory(androidViewModel)).emaViewModel as VM

    LaunchedEffect(Unit) {
        vm.onStart(initializer)
    }

    val state = vm.getObservableState()
        .collectAsState(initial = EmaState.Normal(defaultState)).value

    when (state) {
        is EmaState.Normal -> {
            screenView.onStateNormal(state.data,screenActions)
        }
        is EmaState.Overlayed -> {
            screenView.onStateNormal(state.data,screenActions)
            screenView.onStateOverlayed(state.dataOverlayed,screenActions)
        }
    }

    LaunchedEffect(Unit) {
        vm.getNavigationState().collect { destination ->
            navigator?.let { nav ->
                if (destination == null)
                    nav.navigateBack()
                else
                    nav.navigate(destination)
            }
        }
    }

    val event = vm.getSingleObservableState().collectAsState(initial = EmaExtraData())
    screenView.onSingleEvent(event.value,screenActions)
}