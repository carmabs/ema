package com.carmabs.ema.presentation.dialog.error

import android.view.LayoutInflater
import android.view.ViewGroup
import com.carmabs.ema.android.extension.string
import com.carmabs.ema.android.ui.dialog.EmaDialog
import com.carmabs.ema.presentation.dialog.error.ErrorDialogData
import com.carmabs.ema.sample.ema.databinding.DialogErrorBinding

/**
 * Simple dialog
 *
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
class ErrorDialog : EmaDialog<DialogErrorBinding, ErrorDialogData>() {

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogErrorBinding {
        return DialogErrorBinding.inflate(inflater,container,false)
    }

    override fun DialogErrorBinding.setup(data: ErrorDialogData) {
        tvDialogErrorTitle.text = data.title.string(requireContext())
        tvDialogErrorMessage.text = data.message.string(requireContext())
        (dialogListener as? ErrorDialogListener)?.also { listener->
            tvDialogErrorAccept.setOnClickListener {
                listener.onConfirmClicked()
            }
        }
        isCancelable = !data.isModal
    }

    override fun createInitialState(): ErrorDialogData {
        return ErrorDialogData()
    }
}