package com.carmabs.ema.presentation.ui.home

import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.viewmodel.EmaViewModel

class HomeViewModel: EmaViewModel<HomeState,HomeDestination>(){
	
	override suspend fun onCreateState(initializer: EmaInitializer?): HomeState {
        return HomeState(emptyList())
    }

    override suspend fun onCreated() {
        super.onCreated()
        navigate(HomeDestination.Profile)
    }
}
