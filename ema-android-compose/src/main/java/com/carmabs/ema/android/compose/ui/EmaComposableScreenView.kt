package com.carmabs.ema.android.compose.ui

import androidx.compose.runtime.Composable
import com.carmabs.ema.android.compose.action.EmaComposableScreenActions
import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.viewmodel.EmaViewModel


interface EmaComposableScreenView<S : EmaDataState, VM : EmaViewModel<S, D>, D : EmaDestination, A: EmaComposableScreenActions>{

    @Composable
    fun onStateOverlayed(extraData: EmaExtraData,actions: A) = Unit

    @Composable
    fun onSingleEvent(extraData: EmaExtraData,actions: A) = Unit

    @Composable
    fun onStateNormal(state: S,actions:A)
}