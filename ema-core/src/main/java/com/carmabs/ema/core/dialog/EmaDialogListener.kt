package com.carmabs.ema.core.dialog

/**
 * Listener based on back OS button or confirm click
 *
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
interface EmaDialogListener {

    fun onOutsidePressed(){
        onBackPressed()
    }
    fun onBackPressed()
    fun onDestroyed() = Unit
}