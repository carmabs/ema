package com.carmabs.ema.presentation.base

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.viewmodel.EmaViewModelAction
import com.carmabs.ema.presentation.dialog.error.ErrorDialogAction
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogAction

/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

abstract class BaseViewModel<S : EmaDataState, A : EmaAction.Screen, N : EmaNavigationEvent>(
    initialDataState: S
) : EmaViewModelAction<S, A, N>(initialDataState) {

    companion object {
        const val OVERLAPPED_ERROR = "OVERLAPPED_ERROR"
        const val OVERLAPPED_DIALOG = "OVERLAPPED_DIALOG"
        const val OVERLAPPED_LOADING = "OVERLAPPED_LOADING"
    }

    fun showLoading() {
        updateToOverlappedState(EmaExtraData(OVERLAPPED_LOADING))
    }

    fun showError(data: Any) {
        updateToOverlappedState(EmaExtraData(id = OVERLAPPED_ERROR, data = data))
    }

    fun showSimpleDialog(data: Any) {
        updateToOverlappedState(EmaExtraData(OVERLAPPED_DIALOG, data = data))
    }

    fun hideError() {
        updateToNormalState()
    }

    fun hideLoading() {
        updateToNormalState()
    }

    fun hideDialog() {
        updateToNormalState()
    }
}