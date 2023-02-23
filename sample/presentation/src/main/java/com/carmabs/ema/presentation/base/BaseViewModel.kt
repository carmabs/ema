package com.carmabs.ema.presentation.base

import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaExtraDialogData
import com.carmabs.ema.core.viewmodel.EmaViewModel
import com.carmabs.ema.presentation.dialog.error.ErrorDialogData
import com.carmabs.ema.presentation.dialog.error.ErrorDialogListener
import com.carmabs.ema.presentation.dialog.loading.LoadingDialogData
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogData
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogListener

/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

abstract class BaseViewModel<S:EmaDataState, D : EmaDestination> : EmaViewModel<S, D>(){

    companion object{
        const val OVERLAPPED_ERROR = "OVERLAPPED_ERROR"
        const val OVERLAPPED_DIALOG = "OVERLAPPED_DIALOG"
        const val OVERLAPPED_LOADING = "OVERLAPPED_LOADING"
    }
    fun showLoading(data: LoadingDialogData?=null){
        updateToOverlappedState(EmaExtraData(OVERLAPPED_LOADING,data))
    }

    fun showError(data: ErrorDialogData,listener:ErrorDialogListener){
        updateToOverlappedState(EmaExtraData(OVERLAPPED_ERROR,EmaExtraDialogData(data,listener)))
    }

    fun showDialog(data: SimpleDialogData,listener: SimpleDialogListener){
        updateToOverlappedState(EmaExtraData(OVERLAPPED_DIALOG,EmaExtraDialogData(data,listener)))
    }

    fun hideError(){
        updateToNormalState()
    }

    fun hideLoading(){
        updateToNormalState()
    }

    fun hideDialog(){
        updateToNormalState()
    }
}