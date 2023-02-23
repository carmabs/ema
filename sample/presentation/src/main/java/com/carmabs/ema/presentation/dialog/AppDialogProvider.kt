package com.carmabs.ema.presentation.dialog

import androidx.fragment.app.FragmentManager
import com.carmabs.ema.android.ui.dialog.EmaAndroidDialogProvider
import com.carmabs.ema.android.ui.dialog.EmaDialog
import com.carmabs.ema.core.dialog.EmaDialogData
import com.carmabs.ema.presentation.dialog.error.ErrorDialogData
import com.carmabs.ema.presentation.dialog.error.ErrorDialogProvider
import com.carmabs.ema.presentation.dialog.loading.LoadingDialogData
import com.carmabs.ema.presentation.dialog.loading.LoadingDialogProvider
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogData
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogProvider

class AppDialogProvider(
    fragmentManager: FragmentManager,
    private val simpleDialogProvider: SimpleDialogProvider,
    private val loadingDialogProvider: LoadingDialogProvider,
    private val errorDialogProvider: ErrorDialogProvider
) : EmaAndroidDialogProvider(fragmentManager) {

    override fun generateDialog(dialogData: EmaDialogData?): EmaDialog<*, *> {
        val provider =  when (dialogData) {
            is SimpleDialogData -> simpleDialogProvider.generateDialog(dialogData)
            is LoadingDialogData -> loadingDialogProvider.generateDialog(dialogData)
            is ErrorDialogData -> errorDialogProvider.generateDialog(dialogData)
            else -> throw java.lang.IllegalStateException("Dialog data: $dialogData is not implemented")
        }
        provider.dialogListener = dialogListener
        return provider
    }
}