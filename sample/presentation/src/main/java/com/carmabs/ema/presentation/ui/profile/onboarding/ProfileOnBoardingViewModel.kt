package com.carmabs.ema.presentation.ui.profile.onboarding

import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.presentation.base.BaseViewModel
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationInitializer

class ProfileOnBoardingViewModel: BaseViewModel<ProfileOnBoardingState, ProfileOnBoardingDestination>(), ProfileOnBoardingScreenActions {
	
	override suspend fun onCreateState(initializer: EmaInitializer?): ProfileOnBoardingState {
        return when(val onBoardingInitializer = initializer as? ProfileOnBoardingInitializer){
            is ProfileOnBoardingInitializer.Default -> {
                ProfileOnBoardingState(user = onBoardingInitializer.admin)
            }
            null -> ProfileOnBoardingState()
        }
    }

    override fun onActionAdminClicked() {
        navigate(ProfileOnBoardingDestination.ProfileCreation(ProfileCreationInitializer.Admin))
    }

    override fun onActionUserClicked() {
        navigate(ProfileOnBoardingDestination.ProfileCreation(ProfileCreationInitializer.User))
    }


}
