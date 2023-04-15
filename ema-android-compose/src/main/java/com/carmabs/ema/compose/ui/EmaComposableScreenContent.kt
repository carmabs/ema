package com.carmabs.ema.compose.ui

import android.content.Context
import androidx.compose.runtime.Composable
import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.action.EmaActionDispatcher
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData


interface EmaComposableScreenContent<S : EmaDataState, A : EmaAction> {

    @Composable
    fun onStateOverlapped(extra: EmaExtraData, actions: EmaActionDispatcher<A>) = Unit

    suspend fun onSingleEvent(
        localContext: Context,
        extraData: EmaExtraData,
        actions: EmaActionDispatcher<A>
    ) = Unit

    @Composable
    fun onStateNormal(state: S, actions: EmaActionDispatcher<A>)
}