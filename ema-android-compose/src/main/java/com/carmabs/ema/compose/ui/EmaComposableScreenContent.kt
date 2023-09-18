package com.carmabs.ema.compose.ui

import android.content.Context
import androidx.compose.runtime.Composable
import com.carmabs.ema.compose.action.EmaImmutableActionDispatcher
import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData


interface EmaComposableScreenContent<S : EmaDataState, A : EmaAction> {

    @Composable
    fun onStateOverlapped(extra: EmaExtraData, actions: EmaImmutableActionDispatcher<A>) = Unit

    suspend fun onSingleEvent(
        context: Context,
        extraData: EmaExtraData,
        actions: EmaImmutableActionDispatcher<A>
    ) = Unit

    @Composable
    fun onStateNormal(state: S, actions: EmaImmutableActionDispatcher<A>)
}