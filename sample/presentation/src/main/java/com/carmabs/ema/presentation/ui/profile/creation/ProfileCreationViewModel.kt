package com.carmabs.ema.presentation.ui.profile.creation

import com.carmabs.domain.model.Role
import com.carmabs.domain.model.User
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.presentation.base.BaseViewModel

class ProfileCreationViewModel(
    initialDataState: ProfileCreationState
) : BaseViewModel<ProfileCreationState, ProfileCreationAction, ProfileCreationNavigationEvent>(
    initialDataState
) {
    override fun onStateCreated(initializer: EmaInitializer?) {
        when (initializer as ProfileCreationInitializer) {
            ProfileCreationInitializer.Admin -> updateState {
                copy(role = Role.ADMIN)
            }

            ProfileCreationInitializer.UserBasic -> updateState {
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
            is ProfileCreationAction.UserSurnameWritten -> onActionUserSurnameWritten(action.surname)
            ProfileCreationAction.OnBack -> onActionBack()
            ProfileCreationAction.DialogBackCancel -> onActionBackCancel()
            ProfileCreationAction.DialogBackConfirm -> onActionBackConfirmed()
        }
    }

    private fun onActionBackCancel() {
        updateToNormalState()
    }

    private fun onActionBackConfirmed() {
        updateToNormalState()
        navigate(ProfileCreationNavigationEvent.DialogConfirmationAccepted)
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
        showSimpleDialog(ProfileCreationOverlap.DialogUserCreated(stateData.role))
    }

    private fun onActionDialogConfirmClicked() {
        updateToNormalState()
        addBroadcast(User(stateData.name, stateData.surname, stateData.role))
        navigate(ProfileCreationNavigationEvent.DialogConfirmationAccepted)
    }

    private fun onActionDialogCancelClicked() {
        updateToNormalState()
    }


    private fun onActionBack() {
        showSimpleDialog(ProfileCreationOverlap.DialogBackConfirmation)
    }
}
