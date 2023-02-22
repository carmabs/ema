package com.carmabs.ema.presentation.ui.feature

import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.viewmodel.EmaViewModel

class SettingViewModel: EmaViewModel<SettingState,SettingDestination>(){
	
	override suspend fun onCreateState(initializer: EmaInitializer?): SettingState {
        return SettingState()
        addResult(5, codeId = "pepe")
    }
   
}
