package com.carmabs.ema.android.ui.dialog

import android.util.Log
import androidx.fragment.app.FragmentManager
import com.carmabs.ema.core.dialog.EmaDialogData
import com.carmabs.ema.core.dialog.EmaDialogListener
import com.carmabs.ema.core.dialog.EmaDialogProvider

/**
 * Simple dialog implementation
 *
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
abstract class EmaAndroidDialogProvider constructor(
    private val fragmentManager: FragmentManager,
    private val dialogTag: String? = null
) :
    EmaDialogProvider {

    private var dialog: EmaDialog<*, EmaDialogData>? = null

    abstract fun generateDialog(dialogData: EmaDialogData?): EmaDialog<*, *>

    override fun show(dialogData: EmaDialogData?) {

        //We use this to avoid fragment duplication due to internal fragment save state. It guarantees that on rotation
        //the same fragment is updated and not generate a new one
        fragmentManager.findFragmentByTag(tag)?.also {
            dialog = (it as EmaDialog<*, EmaDialogData>).apply {
                updateDialogData(this, dialogData)
            }
        } ?: also {
            val dialogToUpdate = dialog ?: let {
                val newDialog = (generateDialog(dialogData) as EmaDialog<*, EmaDialogData>)
                if (!newDialog.isVisible && !newDialog.isAdded) {
                    newDialog.show(
                        fragmentManager,
                        tag
                    )
                }
                newDialog
            }
            updateDialogData(dialogToUpdate,dialogData)
            dialog = dialogToUpdate
    }
}

private fun updateDialogData(
    dialog: EmaDialog<*, EmaDialogData>,
    dialogData: EmaDialogData?
) {
    dialog.dialogListener = dialogListener
    dialogData?.also {
        dialog.updateData {
            it
        }
    }
}

override fun hide() {
    //It guarantees that fragment is totally destroyed when it is hidden. Otherwise, the fragment could be saved
    //internally and create duplications on fragment recreations.
    dialog?.let {
        if (!it.isHidden) {
            Log.d(tag, "Alternative dialog totally hidden")
            it.dismissAllowingStateLoss()
        }
        fragmentManager.beginTransaction().remove(it).commitNowAllowingStateLoss()
    }


    dialog = null
}


override val isVisible: Boolean
    get() = fragmentManager.findFragmentByTag(tag)?.isVisible ?: false

override var dialogListener: EmaDialogListener? = null
    set(value) {
        field = value
        dialog?.dialogListener = value
    }

private val tag by lazy {
    dialogTag ?: javaClass.name.toString()
}


}