package com.carmabs.ema.presentation.ui.profile.creation

import com.carmabs.ema.android.compose.action.EmaComposableScreenActions

interface ProfileCreationScreenActions : EmaComposableScreenActions {


    fun onActionUserNameWritten(name: String)

    fun onActionUserSurnameWritten(surname:String)

    fun onActionCreateClicked()

    fun onActionDialogConfirmClicked()

    fun onActionDialogCancelClicked()
}