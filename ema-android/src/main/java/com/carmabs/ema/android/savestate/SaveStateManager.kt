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
abstract class SaveStateManager<S : EmaDataState, A : EmaAction.Screen, N : EmaNavigationEvent> :
    InitializerRepository, SaveStateHandler<S, A, N> {
    override fun onSaveStateHandling(
        scope: CoroutineScope,
        saveStateHandle: SavedStateHandle,
        viewModel: EmaViewModelAction<S, A, N>
    ) = Unit
}

fun <S : EmaDataState, A : EmaAction.Screen, N : EmaNavigationEvent> emaSaveStateManagerOf(
    initializerHandler: InitializerRepository,
    saveStateHandler: SaveStateHandler<S, A, N>? = null
): SaveStateManager<S, A, N> {
    return object : SaveStateManager<S, A, N>() {
        override fun save(arcInitializer: EmaInitializer, savedStateHandle: SavedStateHandle) {
            initializerHandler.save(arcInitializer, savedStateHandle)
        }

        override fun restore(savedStateHandle: SavedStateHandle): EmaInitializer {
            return initializerHandler.restore(savedStateHandle)
        }

        override fun onSaveStateHandling(
            scope: CoroutineScope,
            saveStateHandle: SavedStateHandle,
            viewModel: EmaViewModelAction<S, A, N>
        ) {
            saveStateHandler?.onSaveStateHandling(scope, saveStateHandle, viewModel)
        }
    }
}
