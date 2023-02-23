package com.carmabs.ema.presentation.dialog.simple

import androidx.fragment.app.FragmentManager
import com.carmabs.ema.android.ui.dialog.EmaAndroidDialogProvider
import com.carmabs.ema.android.ui.dialog.EmaDialog
import com.carmabs.ema.core.dialog.EmaDialogData

/**
 * Simple dialog implementation
 *
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */

class SimpleDialogProvider constructor(fragmentManager: FragmentManager) : EmaAndroidDialogProvider(fragmentManager) {
    override fun generateDialog(dialogData: EmaDialogData?): EmaDialog<*,*> = SimpleDialog()
}