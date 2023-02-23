package com.carmabs.ema.android.compose.ui

import android.content.Context
import androidx.compose.runtime.Composable
import com.carmabs.ema.android.compose.action.EmaComposableScreenActions
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData


interface EmaComposableScreenContent<S : EmaDataState, A : EmaComposableScreenActions> {

    @Composable
    fun onStateOverlapped(extra: EmaExtraData, actions: A) = Unit

    suspend fun onSingleEvent(localContext: Context, extraData: EmaExtraData, actions: A) = Unit

    @Composable
    fun onStateNormal(state: S, actions: A)
}