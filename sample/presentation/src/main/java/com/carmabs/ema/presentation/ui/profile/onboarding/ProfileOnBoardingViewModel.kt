package com.carmabs.ema.presentation.ui.profile.onboarding

import com.carmabs.ema.core.action.EmaActionDispatcher
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.presentation.base.BaseViewModel
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationInitializer

class ProfileOnBoardingViewModel :
    BaseViewModel<ProfileOnBoardingState, ProfileOnBoardingDestination>(),
    EmaActionDispatcher<ProfileOnBoardingActions> {

    override suspend fun onCreateState(initializer: EmaInitializer?): ProfileOnBoardingState {
        return when (val onBoardingInitializer = initializer as? ProfileOnBoardingInitializer) {
            is ProfileOnBoardingInitializer.Default -> {
                ProfileOnBoardingState(user = onBoardingInitializer.admin)
            }
            null -> ProfileOnBoardingState()
        }
    }


    private fun onActionAdminClicked() {
        navigate(
            ProfileOnBoardingDestination.ProfileCreation()
                .setInitializer(ProfileCreationInitializer.Admin)
        )
    }

    private fun onActionUserClicked() {
        navigate(
            ProfileOnBoardingDestination.ProfileCreation()
                .setInitializer(ProfileCreationInitializer.User)
        )
    }

    override fun onAction(action: ProfileOnBoardingActions) {
        when (action) {
            ProfileOnBoardingActions.AdminClicked -> onActionAdminClicked()
            ProfileOnBoardingActions.UserClicked -> onActionUserClicked()
        }
    }


}
