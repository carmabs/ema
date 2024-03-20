package com.carmabs.ema.presentation.ui.profile.onboarding

import com.carmabs.domain.model.Role
import com.carmabs.domain.model.User
import com.carmabs.ema.core.action.EmaActionDispatcher
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.presentation.base.BaseViewModel
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationInitializer

class ProfileOnBoardingViewModel(initialDataState: ProfileOnBoardingState) : BaseViewModel<ProfileOnBoardingState, ProfileOnBoardingActions, ProfileOnBoardingNavigationEvent>(
        initialDataState
    ){

    override fun onStateCreated(initializer: EmaInitializer?) {
        when (val onBoardingInitializer = initializer as? ProfileOnBoardingInitializer) {
            is ProfileOnBoardingInitializer.Default -> {
                updateState {
                    copy(user = User(onBoardingInitializer.admin))
                }
            }

            null -> {
                //DO NOTHING
            }
        }
    }

    override fun onAction(action: ProfileOnBoardingActions) {
        when (action) {
            ProfileOnBoardingActions.AdminClicked -> onActionAdminClicked()
            ProfileOnBoardingActions.UserClicked -> onActionUserClicked()
        }
    }
    private fun onActionAdminClicked() {
        navigate(
            ProfileOnBoardingNavigationEvent.ProfileCreation(Role.ADMIN)
        )
    }

    private fun onActionUserClicked() {
        navigate(
            ProfileOnBoardingNavigationEvent.ProfileCreation(Role.BASIC)
        )
    }
}
