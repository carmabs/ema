package com.carmabs.ema.presentation.ui.profile.onboarding

import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.viewmodel.EmaViewModel
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationInitializer
import kotlinx.coroutines.delay

class ProfileOnBoardingViewModel: EmaViewModel<ProfileOnBoardingState, ProfileOnBoardingDestination>(), ProfileOnBoardingScreenActions {
	
	override suspend fun onCreateState(initializer: EmaInitializer?): ProfileOnBoardingState {
        return when(val onBoardingInitializer = initializer as? ProfileOnBoardingInitializer){
            is ProfileOnBoardingInitializer.Default -> {
                ProfileOnBoardingState(user = onBoardingInitializer.admin)
            }
            null -> ProfileOnBoardingState()
        }
    }

    override suspend fun onStateCreated() {
        super.onStateCreated()
        executeUseCase {
            repeat(3){
                delay(2000)
                notifySingleEvent(EmaExtraData(data = "Sample event $it"))
            }
        }

    }

    override fun onActionAdminClicked() {
        navigate(ProfileOnBoardingDestination.ProfileCreation(ProfileCreationInitializer.Admin))
    }

    override fun onActionUserClicked() {
        navigate(ProfileOnBoardingDestination.ProfileCreation(ProfileCreationInitializer.User))
    }


}
