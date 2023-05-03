package com.carmabs.ema.presentation.ui.profile.creation

import com.carmabs.domain.manager.ResourceManager
import com.carmabs.domain.model.Role
import com.carmabs.domain.model.User
import com.carmabs.ema.core.action.EmaActionDispatcher
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.navigator.EmaEmptyDestination
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.presentation.base.BaseViewModel
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogData
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogListener

class ProfileCreationViewModel(
    private val resourceManager: ResourceManager
) : BaseViewModel<ProfileCreationState, EmaEmptyDestination>(), EmaActionDispatcher<ProfileCreationActions> {

    companion object {
        const val OVERLAPPED_DIALOG_CONFIRMATION = "OVERLAPPED_DIALOG_CONFIRMATION"
    }

    override suspend fun onCreateState(initializer: EmaInitializer?): ProfileCreationState {
        return when (val init = initializer as ProfileCreationInitializer) {
            ProfileCreationInitializer.Admin -> ProfileCreationState(
                Role.ADMIN
            )
            ProfileCreationInitializer.User -> ProfileCreationState(
                Role.BASIC
            )
        }
    }

    override fun onAction(action: ProfileCreationActions) {
        when(action){
            ProfileCreationActions.CreateClicked -> onActionCreateClicked()
            ProfileCreationActions.DialogCancelClicked -> onActionDialogCancelClicked()
            ProfileCreationActions.DialogConfirmClicked -> onActionDialogConfirmClicked()
            is ProfileCreationActions.UserNameWritten -> onActionUserNameWritten(action.name)
            is ProfileCreationActions.UserSurnameWritten ->onActionUserNameWritten(action.surname)
        }
    }

    private fun onActionUserNameWritten(name: String) {
        updateToNormalState {
            copy(name = name)
        }
    }

    private fun onActionUserSurnameWritten(surname: String) {
        updateToNormalState {
            copy(surname = surname)
        }
    }

    private fun onActionCreateClicked() {
        val image = when (stateData.role) {
            Role.ADMIN -> resourceManager.getAdminImage()
            Role.BASIC -> resourceManager.getUserImage()
        }
        val title = when (stateData.role) {
            Role.ADMIN -> resourceManager.getCreateUserAdminTitle()
            Role.BASIC -> resourceManager.getCreateUserBasicTitle()
        }
        updateToOverlappedState(
            EmaExtraData(
                OVERLAPPED_DIALOG_CONFIRMATION, SimpleDialogData(
                    title = title,
                    message = resourceManager.getCreateUserMessage(),
                    showCancel = true,
                    image = image,
                    proportionWidth = 0.9f
                )
            )
        )
        addResult(User(stateData.name, stateData.surname, stateData.role))
    }

    private fun onActionDialogConfirmClicked() {
        updateToNormalState()
        navigateBack()
    }

    private fun onActionDialogCancelClicked() {
        updateToNormalState()
    }

    override val onBackHardwarePressedListener: (() -> Boolean) = {
        showDialog(
            SimpleDialogData(
                title = resourceManager.getDoYouWantToExitTitleCreationUserTitle(),
                message = resourceManager.getDoYouWantToExitTitleCreationUserMessage(),
                proportionWidth = 0.8f,
                showCancel = true,
                image = resourceManager.getExitImage()
            ),
            object : SimpleDialogListener{
                override fun onCancelClicked() {
                   updateToNormalState()
                }

                override fun onConfirmClicked() {
                    updateToNormalState()
                    navigateBack()
                }

                override fun onBackPressed() {
                   updateToNormalState()
                }

            }
        )
        false
    }
}