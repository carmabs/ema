package com.carmabs.ema.presentation.base

import androidx.viewbinding.ViewBinding
import com.carmabs.ema.android.ui.EmaFragment
import com.carmabs.ema.core.model.EmaText
import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaExtraDialogData
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

abstract class BaseFragment<B : ViewBinding, S : EmaDataState, VM : EmaViewModel<S, D>, D : EmaDestination> :
    EmaFragment<B, S, VM, D>() {


    private val appDialogProvider: AppDialogProvider by inject {
        parametersOf(childFragmentManager)
    }

    final override fun B.onStateNormal(data: S) {
        hideLoading()
        hideError()
        hideDialog()
        onNormal(data)
    }

    final override fun B.onStateOverlapped(extra: EmaExtraData) {
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
                onOverlayed(extra)
            }
        }

    }

    protected fun showLoading(
        data: LoadingDialogData? = null
    ) {
        val loadingData = data ?: LoadingDialogData(
            EmaText.id(R.string.dialog_loading_title),
            EmaText.id(R.string.dialog_loading_message)
        )
        appDialogProvider.show(loadingData)
    }

    protected fun showDialog(dialogData: SimpleDialogData, listener: SimpleDialogListener) {
        appDialogProvider.show(dialogData)
        appDialogProvider.dialogListener = listener
    }

    protected fun showError(errorDialogData: ErrorDialogData, listener: ErrorDialogListener) {
        appDialogProvider.show(errorDialogData)
        appDialogProvider.dialogListener = listener
    }

    final override fun B.onSingleEvent(extra: EmaExtraData) {
        onSingle(extra)
    }

    abstract fun B.onNormal(data: S)

    protected open fun B.onOverlayed(extra: EmaExtraData) {}

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