package com.carmabs.ema.android.savestate

import androidx.lifecycle.SavedStateHandle
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaViewModel
import kotlinx.coroutines.CoroutineScope

/**
 * Created by Carlos Mateo Benito on 22/2/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.comm”>Carlos Mateo Benito</a>
 */
interface SaveStateManager<S : EmaDataState, N : EmaNavigationEvent> {
    fun onSaveStateHandling(
        scope: CoroutineScope,
        saveStateHandle: SavedStateHandle,
        viewModel: EmaViewModel<S, N>
    )
}

fun <S : EmaDataState, N : EmaNavigationEvent> emaSaveStateManager(
    onSaveStateHandling: (CoroutineScope, SavedStateHandle, EmaViewModel<S, N>) -> Unit
): SaveStateManager<S, N> {
    return object : SaveStateManager<S, N> {
        override fun onSaveStateHandling(
            scope: CoroutineScope,
            saveStateHandle: SavedStateHandle,
            viewModel: EmaViewModel<S, N>
        ) {
            onSaveStateHandling.invoke(scope, saveStateHandle, viewModel)
        }

    }
}
