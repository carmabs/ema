package com.carmabs.ema.presentation.base.compose

import androidx.compose.runtime.Composable
import com.carmabs.ema.compose.action.EmaComposableScreenActions
import com.carmabs.ema.compose.ui.EmaComposableScreenContent
import com.carmabs.ema.core.model.EmaText
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaExtraDialogData
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

abstract class BaseScreenComposable<S : EmaDataState, A : EmaComposableScreenActions> :
    EmaComposableScreenContent<S, A> {

    @Composable
    final override fun onStateNormal(state: S, actions: A) {
        onNormal(state = state, actions = actions)
    }

    @Composable
    final override fun onStateOverlapped(extra: EmaExtraData, actions: A) {
        super.onStateOverlapped(extra, actions)
        when (extra.id) {
            BaseViewModel.OVERLAPPED_LOADING -> {
                showLoading(extra.data as? LoadingDialogData)
            }
            BaseViewModel.OVERLAPPED_ERROR -> {
                val dialogData = extra.data as EmaExtraDialogData
                val data = dialogData.data as ErrorDialogData
                val listener = dialogData.listener as ErrorDialogListener
                showError(data, listener)
            }
            BaseViewModel.OVERLAPPED_DIALOG -> {
                val dialogData = extra.data as EmaExtraDialogData
                val data = dialogData.data as SimpleDialogData
                val listener = dialogData.listener as SimpleDialogListener
                showDialog(data, listener)
            }
            else -> {
                onOverlapped(extra,actions)
            }
        }
    }

    @Composable
    open fun onOverlapped(extra: EmaExtraData,actions: A) = Unit

    @Composable
    abstract fun onNormal(state: S, actions: A)

    @Composable
    protected fun showDialog(data: SimpleDialogData, listener: SimpleDialogListener) {
        SimpleDialogComposable(dialogData = data, dialogListener = listener)
    }

    @Composable
    protected fun showError(data: ErrorDialogData, listener: ErrorDialogListener) {
        ErrorDialogComposable(dialogData = data, dialogListener = listener)
    }

    @Composable
    protected fun showLoading(loadingDialogData: LoadingDialogData?) {
        LoadingDialogComposable(
            dialogData = loadingDialogData ?: LoadingDialogData(
                title = EmaText.id(id = R.string.dialog_loading_title),
                message = EmaText.id(id = R.string.dialog_loading_message)
            )
        )
    }
}