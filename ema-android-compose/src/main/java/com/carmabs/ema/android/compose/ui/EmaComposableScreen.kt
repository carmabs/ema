package com.carmabs.ema.android.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.core.viewmodel.EmaViewModel

@Composable
fun <S : EmaDataState, VM : EmaViewModel<S, D>, D : EmaDestination> EmaComposableScreen(
    vm: VM,
    initializer: EmaInitializer? = null,
    navigator: EmaNavigator<D>? = null,
    onStateOverlayed: @Composable ((EmaExtraData) -> Unit)? = null,
    onSingleEvent: @Composable ((EmaExtraData) -> Unit)? = null,
    onStateNormal: @Composable ((S) -> Unit)
) {
    LaunchedEffect(Unit) {
        vm.onStart(initializer)
    }

    val state = vm.getObservableState().collectAsState(initial = EmaState.Normal(object: EmaDataState{} as S)).value

    when(state){
        is EmaState.Normal -> {
            onStateNormal.invoke(state as S)
        }
        is EmaState.Overlayed -> {
            onStateNormal.invoke(state as S)
            onStateOverlayed?.invoke(state.dataOverlayed)
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
    onSingleEvent?.invoke(event.value)
}