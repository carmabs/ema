package com.carmabs.ema.presentation.dialog.loading

import androidx.fragment.app.FragmentManager
import com.carmabs.ema.android.ui.dialog.EmaAndroidDialogProvider
import com.carmabs.ema.android.ui.dialog.EmaDialog
import com.carmabs.ema.core.dialog.EmaDialogData
import com.carmabs.ema.presentation.dialog.error.ErrorDialog

/**
 * Simple dialog implementation
 *
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */

class LoadingDialogProvider constructor(fragmentManager: FragmentManager) : EmaAndroidDialogProvider(fragmentManager)
{
    override fun generateDialog(dialogData: EmaDialogData?): EmaDialog<*,*> =  LoadingDialog()
}