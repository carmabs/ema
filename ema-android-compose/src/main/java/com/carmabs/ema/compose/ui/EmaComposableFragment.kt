package com.carmabs.ema.compose.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import com.carmabs.ema.android.base.EmaCoreFragment
import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.core.viewmodel.EmaViewModel


/**
 * Base fragment to bind and unbind view model
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
abstract class EmaComposableFragment<S : EmaDataState, VM : EmaViewModel<S, D>, D : EmaDestination>
    : EmaCoreFragment<S, VM, D>() {

    protected var isFirstNormalExecution: Boolean = true
        private set

    protected var isFirstOverlayedExecution: Boolean = true
        private set

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        isFirstNormalExecution = true
        isFirstOverlayedExecution = true
        return ComposeView(requireContext()).apply {
            setContent {
                val state = vm.getObservableState()
                    .collectAsState(initial = EmaState.Normal(object : EmaDataState {} as S))
                when (val emaState = state.value) {
                    is EmaState.Normal<S> -> {
                        onStateNormal(data = emaState.data)
                        isFirstNormalExecution = false
                    }
                    is EmaState.Overlapped<S> -> {
                        onStateNormal(data = emaState.data)
                        onStateOverlapped(data = emaState.dataOverlapped)
                        isFirstOverlayedExecution = false
                    }

                }
            }
        }
    }

    @Composable
    abstract fun onStateNormal(data: S)

    @Composable
    protected open fun onStateOverlapped(data: EmaExtraData) = Unit

    // Discard these methods because they are called now by compose
    final override fun onEmaStateNormal(data: S) = Unit

    final override fun onEmaStateOverlapped(extra: EmaExtraData) = Unit
}