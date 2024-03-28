package com.carmabs.ema.presentation.dialog.simple

import com.carmabs.ema.core.dialog.EmaDialogListener

/**
 * Listener based on back OS button or confirm click
 *
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
interface SimpleDialogListener : EmaDialogListener {

    companion object{
        val EMPTY = object : SimpleDialogListener{
            override fun onCancelClicked() = Unit
            override fun onConfirmClicked() = Unit
            override fun onBackPressed() = Unit
        }
    }
    fun onCancelClicked()
    fun onConfirmClicked()

}