package com.carmabs.ema.presentation.dialog.loading

import android.view.LayoutInflater
import android.view.ViewGroup
import com.carmabs.ema.android.ui.dialog.EmaDialog
import com.carmabs.ema.presentation.dialog.error.ErrorDialogData
import com.carmabs.ema.sample.ema.databinding.DialogLoadingBinding

/**
 * Simple dialog
 *
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
class LoadingDialog : EmaDialog<DialogLoadingBinding, LoadingDialogData>() {

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogLoadingBinding {
        return DialogLoadingBinding.inflate(inflater,container,false)
    }

    override fun DialogLoadingBinding.setup(data: LoadingDialogData) {
        tvDialogLoadingTitle.text = data.title
        tvDialogLoadingMessage.text = data.message

        isCancelable = !data.isModal
    }

    override fun createInitialState(): LoadingDialogData {
        return LoadingDialogData()
    }
}