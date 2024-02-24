package com.carmabs.ema.android.savestate

import androidx.lifecycle.SavedStateHandle
import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaViewModelAction
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
abstract class SaveStateManager<A : EmaAction.Screen, S : EmaDataState, N : EmaNavigationEvent> :
    InitializerRepository, SaveStateHandler<A, S, N> {
    override fun onSaveStateHandling(
        scope: CoroutineScope,
        saveStateHandle: SavedStateHandle,
        viewModel: EmaViewModelAction<A, S, N>
    ) = Unit
}

fun <A : EmaAction.Screen, S : EmaDataState, N : EmaNavigationEvent> emaSaveStateManagerOf(
    initializerHandler: InitializerRepository,
    saveStateHandler: SaveStateHandler<A, S, N>? = null
): SaveStateManager<A, S, N> {
    return object : SaveStateManager<A, S, N>() {
        override fun save(arcInitializer: EmaInitializer, savedStateHandle: SavedStateHandle) {
            initializerHandler.save(arcInitializer, savedStateHandle)
        }

        override fun restore(savedStateHandle: SavedStateHandle): EmaInitializer {
            return initializerHandler.restore(savedStateHandle)
        }

        override fun onSaveStateHandling(
            scope: CoroutineScope,
            saveStateHandle: SavedStateHandle,
            viewModel: EmaViewModelAction<A, S, N>
        ) {
            saveStateHandler?.onSaveStateHandling(scope, saveStateHandle, viewModel)
        }
    }
}
