package com.carmabs.ema.core.dialog

/**
 * Abstract class to show a dialog
 *
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
interface EmaDialogProvider {

    fun show(dialogData: EmaDialogData?=null)
    fun hide()

    var dialogListener: EmaDialogListener?

    val isVisible:Boolean



}