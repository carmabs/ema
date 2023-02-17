package com.carmabs.ema.presentation.ui.profile

import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.viewmodel.EmaViewModel

class ProfileViewModel: EmaViewModel<ProfileState, ProfileDestination>(){
	
	override suspend fun onCreateState(initializer: EmaInitializer?): ProfileState {
        return ProfileState()
    }

    fun onActionAdminClicked() {

    }

}
