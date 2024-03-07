package com.carmabs.ema.android.savestate

import androidx.lifecycle.SavedStateHandle
import com.carmabs.ema.core.action.EmaAction
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
interface SaveStateHandler<S : EmaDataState, A : EmaAction.Screen, N : EmaNavigationEvent> {
    fun onSaveStateHandling(
        scope: CoroutineScope,
        saveStateHandle: SavedStateHandle,
        viewModel: EmaViewModelAction<S, A, N>
    )
}

fun <S : EmaDataState, A : EmaAction.Screen, N : EmaNavigationEvent> emaSaveStateHandler(
    onSaveStateHandling: (CoroutineScope, SavedStateHandle, EmaViewModelAction<S, A, N>) -> Unit
): SaveStateHandler<S, A, N> {
    return object : SaveStateHandler<S, A, N> {
        override fun onSaveStateHandling(
            scope: CoroutineScope,
            saveStateHandle: SavedStateHandle,
            viewModel: EmaViewModelAction<S, A, N>
        ) {
            onSaveStateHandling.invoke(scope, saveStateHandle, viewModel)
        }

    }
}
