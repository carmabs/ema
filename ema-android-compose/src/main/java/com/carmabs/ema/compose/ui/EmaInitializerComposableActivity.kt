package com.carmabs.ema.compose.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.carmabs.ema.android.base.EmaCoreActivity
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.core.viewmodel.EmaViewModel


/**
 * Base fragment to bind and unbind view model
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
abstract class EmaComposableActivity<S : EmaDataState, VM : EmaViewModel<S, N>, N : EmaNavigationEvent>
    : EmaCoreActivity<S, VM, N>() {

    protected var isFirstNormalExecution: Boolean = true
        private set

    protected var isFirstOverlayedExecution: Boolean = true
        private set

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isFirstNormalExecution = true
        isFirstOverlayedExecution = true
        setContent {
                val state = viewModel.subscribeStateUpdates()
                    .collectAsState(initial = EmaState.Normal(object : EmaDataState {} as S))
                when (val emaState = state.value) {
                    is EmaState.Normal<S,N> -> {
                        onStateNormal(data = emaState.data)
                        isFirstNormalExecution = false
                    }
                    is EmaState.Overlapped<S,N> -> {
                        onStateNormal(data = emaState.data)
                        onStateOverlapped(extraData = emaState.extraData)
                        isFirstOverlayedExecution = false
                    }

                }
            }
    }

    @Composable
    abstract fun onStateNormal(data: S)

    @Composable
    protected open fun onStateOverlapped(extraData: EmaExtraData) = Unit

    // Discard these methods because they are called now by compose
    final override fun onEmaStateNormal(data: S) = Unit

    final override fun onEmaStateOverlapped(extraData: EmaExtraData) = Unit
}