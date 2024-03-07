package com.carmabs.ema.presentation.ui.profile.creation

import com.carmabs.ema.core.action.EmaAction

sealed interface ProfileCreationAction : EmaAction.Screen {

    data class UserNameWritten(val name: String):ProfileCreationAction

    data class UserSurnameWritten(val surname:String):ProfileCreationAction

   data object CreateClicked:ProfileCreationAction

    data object DialogConfirmClicked:ProfileCreationAction

    data object DialogCancelClicked:ProfileCreationAction

    data object OnBack : ProfileCreationAction
}