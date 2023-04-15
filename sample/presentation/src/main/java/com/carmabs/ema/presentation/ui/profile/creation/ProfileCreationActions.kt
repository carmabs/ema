package com.carmabs.ema.presentation.ui.profile.creation

import com.carmabs.ema.core.action.EmaAction

sealed interface ProfileCreationActions : EmaAction {

    data class UserNameWritten(val name: String):ProfileCreationActions

    data class UserSurnameWritten(val surname:String):ProfileCreationActions

   object CreateClicked:ProfileCreationActions

    object DialogConfirmClicked:ProfileCreationActions

    object DialogCancelClicked:ProfileCreationActions
}