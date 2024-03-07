package com.carmabs.ema.presentation.ui.profile.creation

import com.carmabs.domain.manager.ResourceManager
import com.carmabs.domain.model.Role
import com.carmabs.domain.model.User
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.presentation.base.BaseViewModel
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogData
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogListener

class ProfileCreationViewModel(
    private val resourceManager: ResourceManager,
    initialDataState: ProfileCreationState
) : BaseViewModel<ProfileCreationState, ProfileCreationAction, ProfileCreationNavigationEvent>(
    initialDataState
) {

    companion object {
        const val OVERLAPPED_DIALOG_CONFIRMATION = "OVERLAPPED_DIALOG_CONFIRMATION"
    }

    override fun onStateCreated(initializer: EmaInitializer?) {
        when (initializer as ProfileCreationInitializer) {
            ProfileCreationInitializer.Admin -> updateState {
                copy(role = Role.ADMIN)
            }

            ProfileCreationInitializer.User -> updateState {
                copy(role = Role.BASIC)
            }
        }
    }

    override fun onAction(action: ProfileCreationAction) {
        when (action) {
            ProfileCreationAction.CreateClicked -> onActionCreateClicked()
            ProfileCreationAction.DialogCancelClicked -> onActionDialogCancelClicked()
            ProfileCreationAction.DialogConfirmClicked -> onActionDialogConfirmClicked()
            is ProfileCreationAction.UserNameWritten -> onActionUserNameWritten(action.name)
            is ProfileCreationAction.UserSurnameWritten -> onActionUserNameWritten(action.surname)
            ProfileCreationAction.OnBack -> onActionBack()
        }
    }

    private fun onActionUserNameWritten(name: String) {
        updateState {
            copy(name = name)
        }
    }

    private fun onActionUserSurnameWritten(surname: String) {
        updateState {
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
        addBroadcast(User(stateData.name, stateData.surname, stateData.role))
    }

    private fun onActionDialogConfirmClicked() {
        updateToNormalState()
        navigate(ProfileCreationNavigationEvent.DialogConfirmationAccepted)
    }

    private fun onActionDialogCancelClicked() {
        updateToNormalState()
    }


    private fun onActionBack() {
        showDialog(
            SimpleDialogData(
                title = resourceManager.getDoYouWantToExitTitleCreationUserTitle(),
                message = resourceManager.getDoYouWantToExitTitleCreationUserMessage(),
                proportionWidth = 0.8f,
                showCancel = true,
                image = resourceManager.getExitImage()
            ),
            object : SimpleDialogListener {
                override fun onCancelClicked() {
                    updateToNormalState()
                }

                override fun onConfirmClicked() {
                    updateToNormalState()
                    navigate(ProfileCreationNavigationEvent.DialogConfirmationAccepted)
                }

                override fun onBackPressed() {
                    updateToNormalState()
                }

            }
        )
    }
}
