package com.carmabs.ema.presentation.dialog.error

import com.carmabs.ema.core.dialog.EmaDialogListener

/**
 * Listener based on back OS button or confirm click
 *
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
interface ErrorDialogListener : EmaDialogListener {
    fun onConfirmClicked()
}