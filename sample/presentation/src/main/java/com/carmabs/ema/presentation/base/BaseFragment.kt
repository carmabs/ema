package com.carmabs.ema.presentation.base

import androidx.viewbinding.ViewBinding
import com.carmabs.ema.android.ui.EmaFragment
import com.carmabs.ema.core.model.EmaText
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.viewmodel.EmaViewModel
import com.carmabs.ema.presentation.dialog.AppDialogProvider
import com.carmabs.ema.presentation.dialog.error.ErrorDialogData
import com.carmabs.ema.presentation.dialog.error.ErrorDialogListener
import com.carmabs.ema.presentation.dialog.loading.LoadingDialogData
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogData
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogListener
import com.carmabs.ema.sample.ema.R
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf


/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

abstract class BaseFragment<B : ViewBinding, S : EmaDataState, VM : EmaViewModel<S, N>, N : EmaNavigationEvent> :
    EmaFragment<B, S, VM, N>() {


    private val appDialogProvider: AppDialogProvider by inject {
        parametersOf(childFragmentManager)
    }


    final override fun B.onStateNormal(data: S) {
        hideLoading()
        hideError()
        hideDialog()
        onNormal(data)
    }

    private fun showLoading(
        data: LoadingDialogData? = null
    ) {
        val loadingData = data ?: LoadingDialogData(
            EmaText.id(R.string.dialog_loading_title),
            EmaText.id(R.string.dialog_loading_message)
        )
        appDialogProvider.show(loadingData)
    }

    final override fun B.onStateOverlapped(extraData: EmaExtraData) {
        when (extraData.id) {
            BaseViewModel.OVERLAPPED_ERROR -> onOverlappedError(extraData)
            BaseViewModel.OVERLAPPED_LOADING -> onOverlappedLoading(extraData)
            BaseViewModel.OVERLAPPED_DIALOG -> onOverlappedDialog(extraData)
            else -> onOverlapped(extraData = extraData)
        }
    }

    protected fun showSimpleDialog(
        dialogData: SimpleDialogData,
        dialogListener: SimpleDialogListener
    ) {
        appDialogProvider.show(dialogData)
        appDialogProvider.dialogListener = dialogListener
    }

    protected fun showError(
        errorDialogData: ErrorDialogData,
        dialogListener: ErrorDialogListener
    ) {
        appDialogProvider.show(errorDialogData)
        appDialogProvider.dialogListener = dialogListener
    }

    final override fun B.onSingleEvent(extraData: EmaExtraData) {
        onSingle(extraData)
    }

    abstract fun B.onNormal(data: S)

    protected open fun B.onOverlapped(extraData: EmaExtraData) = Unit

    protected open fun B.onOverlappedError(extraData: EmaExtraData) = Unit

    protected open fun B.onOverlappedDialog(extraData: EmaExtraData) = Unit

    protected open fun B.onOverlappedLoading(extraData: EmaExtraData){
        showLoading()
    }

    protected open fun B.onSingle(extra: EmaExtraData) {}

    protected fun hideLoading() {
        appDialogProvider.hide()
    }

    protected fun hideError() {
        appDialogProvider.hide()
    }

    protected fun hideDialog() {
        appDialogProvider.hide()
    }

}