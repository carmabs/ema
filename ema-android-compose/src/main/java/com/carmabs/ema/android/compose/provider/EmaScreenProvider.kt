package com.carmabs.ema.android.compose.provider

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.carmabs.ema.android.compose.action.EmaComposableScreenActions
import com.carmabs.ema.android.compose.action.EmaComposableScreenActionsEmpty
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.android.viewmodel.EmaViewModelFactory
import com.carmabs.ema.core.viewmodel.EmaViewModel

class EmaScreenProvider<VM:EmaViewModel<*,*>,A:EmaComposableScreenActions> {

    @Composable
    fun provide(androidViewModel:EmaAndroidViewModel):Pair<VM,A>{
        val vm = viewModel(
            androidViewModel::class.java,
            factory = EmaViewModelFactory(androidViewModel)
        ).emaViewModel as VM
        val actions = (vm as? A)?:EmaComposableScreenActionsEmpty as A

        return  Pair(vm, actions)
    }
}