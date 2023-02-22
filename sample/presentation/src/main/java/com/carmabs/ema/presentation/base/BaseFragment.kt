package com.carmabs.ema.presentation.base

import androidx.viewbinding.ViewBinding
import com.carmabs.ema.android.ui.EmaFragment
import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaExtraDialogData
import com.carmabs.ema.core.viewmodel.EmaViewModel
import com.carmabs.ema.presentation.dialog.error.ErrorDialogData
import com.carmabs.ema.presentation.dialog.error.ErrorDialogListener
import com.carmabs.ema.presentation.dialog.error.ErrorDialogProvider
import com.carmabs.ema.presentation.dialog.loading.LoadingDialogData
import com.carmabs.ema.presentation.dialog.loading.LoadingDialogProvider
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogData
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogListener
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogProvider
import com.carmabs.ema.sample.ema.R


/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

abstract class BaseFragment<B : ViewBinding, S : EmaDataState, VM : EmaViewModel<S, D>, D : EmaDestination> :
    EmaFragment<B, S, VM, D>() {


    private val loadingProvider by lazy {
        LoadingDialogProvider(fragmentManager = childFragmentManager)
    }
    private val errorProvider by lazy {
        ErrorDialogProvider(fragmentManager = childFragmentManager)
    }
    private val dialogProvider by lazy {
        SimpleDialogProvider(fragmentManager = childFragmentManager)
    }
    final override fun B.onStateNormal(data: S) {
        hideLoading()
        hideError()
        hideDialog()
        onNormal(data)
    }

    final override fun B.onStateOverlapped(extra: EmaExtraData) {
        when(extra.id){
            BaseViewModel.OVERLAPPED_LOADING->{
                showLoading(extra.data as? LoadingDialogData)
            }
            BaseViewModel.OVERLAPPED_ERROR->{
                val dialogData = extra.data as EmaExtraDialogData
                val data = dialogData.data as ErrorDialogData
                val listener = dialogData.listener as ErrorDialogListener
                showError(data,listener)
            }
            BaseViewModel.OVERLAPPED_DIALOG->{
                val dialogData = extra.data as EmaExtraDialogData
                val data = dialogData.data as SimpleDialogData
                val listener = dialogData.listener as SimpleDialogListener
                showDialog(data, listener)
            }
            else->{
                onOverlayed(extra)
            }
        }

    }

    private fun showDialog(dialogData: SimpleDialogData,listener:SimpleDialogListener) {
        dialogProvider.show(dialogData)
        dialogProvider.dialogListener = listener
    }

    private fun showError(errorDialogData: ErrorDialogData, listener: ErrorDialogListener) {
        errorProvider.show(errorDialogData)
        errorProvider.dialogListener = listener
    }

    final override fun B.onSingleEvent(extra: EmaExtraData) {
        onSingle(extra)
    }

    abstract fun B.onNormal(data: S)

    protected open fun B.onOverlayed(extra: EmaExtraData) {}

    protected open fun B.onSingle(extra: EmaExtraData) {}


    protected fun showLoading(
        data: LoadingDialogData? = null
    ) {
        val loadingData = data ?: LoadingDialogData(
            getString(R.string.dialog_loading_title),
            getString(R.string.dialog_loading_message)
        )
        loadingProvider.show(loadingData)
    }

    protected fun hideLoading() {
        loadingProvider.hide()
    }

    protected fun hideError() {
        errorProvider.hide()
    }

    protected fun hideDialog() {
        dialogProvider.hide()
    }

}