package com.carmabs.ema.presentation.base.compose

import androidx.compose.runtime.Composable
import com.carmabs.ema.compose.action.EmaImmutableActionDispatcher
import com.carmabs.ema.compose.ui.EmaComposableScreenContent
import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.action.EmaActionDispatcher
import com.carmabs.ema.core.model.EmaText
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.presentation.base.BaseViewModel
import com.carmabs.ema.presentation.dialog.error.ErrorDialogData
import com.carmabs.ema.presentation.dialog.error.ErrorDialogListener
import com.carmabs.ema.presentation.dialog.loading.LoadingDialogData
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogData
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogListener
import com.carmabs.ema.presentation.ui.compose.ErrorDialogComposable
import com.carmabs.ema.presentation.ui.compose.LoadingDialogComposable
import com.carmabs.ema.presentation.ui.compose.SimpleDialogComposable
import com.carmabs.ema.sample.ema.R

abstract class BaseScreenComposable<S : EmaDataState, A : EmaAction.Screen> :
    EmaComposableScreenContent<S, A> {

    @Composable
    final override fun onStateNormal(state: S, actions: EmaImmutableActionDispatcher<A>) {
        onNormal(state = state, actions = actions)
    }

    @Composable
    final override fun onStateOverlapped(extra: EmaExtraData, actions: EmaImmutableActionDispatcher<A>) {
        super.onStateOverlapped(extra, actions)
        when (extra.id) {
            BaseViewModel.OVERLAPPED_LOADING -> {
                onOverlappedLoading(extra.data,actions)
            }
            BaseViewModel.OVERLAPPED_ERROR -> {
                onOverlappedError(extra.data,actions)
            }
            BaseViewModel.OVERLAPPED_DIALOG -> {
                onOverlappedDialog(extra.data,actions)
            }
            else -> {
                onOverlapped(extra,actions)
            }
        }
    }

    @Composable
    protected open fun onOverlappedError(data: Any?, actions: EmaImmutableActionDispatcher<A>) = Unit
    @Composable
    protected open fun onOverlappedDialog(data: Any?, actions: EmaImmutableActionDispatcher<A>) = Unit
    @Composable
    protected open fun onOverlappedLoading(data: Any?, actions: EmaImmutableActionDispatcher<A>) = Unit

    @Composable
    open fun onOverlapped(extra: EmaExtraData,actions: EmaActionDispatcher<A>) = Unit

    @Composable
    abstract fun onNormal(state: S, actions: EmaActionDispatcher<A>)

    @Composable
    protected fun ShowDialog(data: SimpleDialogData,listener: SimpleDialogListener) {
        SimpleDialogComposable(dialogData = data, listener)
    }

    @Composable
    protected fun ShowError(data: ErrorDialogData, listener: ErrorDialogListener) {
        ErrorDialogComposable(dialogData = data, dialogListener = listener)
    }

    @Composable
    protected fun ShowLoading(loadingDialogData: LoadingDialogData?=null) {
        LoadingDialogComposable(
            dialogData = loadingDialogData ?: LoadingDialogData(
                title = EmaText.id(id = R.string.dialog_loading_title),
                message = EmaText.id(id = R.string.dialog_loading_message)
            )
        )
    }
}