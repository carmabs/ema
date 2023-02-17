package com.carmabs.ema.android.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
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
fun <S : EmaDataState, VM : EmaViewModel<S, D>, D : EmaDestination> EmaComposableScreen(
    defaultState:S,
    androidViewModel: EmaAndroidViewModel,
    initializer: EmaInitializer? = null,
    navigator: EmaNavigator<D>? = null,
    onStateOverlayed: @Composable ((vm: VM, extra: EmaExtraData) -> Unit)? = null,
    onSingleEvent: @Composable ((vm: VM, extra: EmaExtraData) -> Unit)? = null,
    onStateNormal: @Composable ((vm: VM, state: S) -> Unit)
) {
    val vm = viewModel(androidViewModel::class.java, factory = EmaViewModelFactory(androidViewModel)).emaViewModel as VM

    LaunchedEffect(Unit) {
        vm.onStart(initializer)
    }

    val state = vm.getObservableState()
        .collectAsState(initial = EmaState.Normal(defaultState)).value

    when (state) {
        is EmaState.Normal -> {
            onStateNormal.invoke(vm, state.data)
        }
        is EmaState.Overlayed -> {
            onStateNormal.invoke(vm, state.data)
            onStateOverlayed?.invoke(vm, state.dataOverlayed)
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
    onSingleEvent?.invoke(vm, event.value)
}