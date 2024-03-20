package com.carmabs.ema.android.initializer.savestate

import androidx.lifecycle.SavedStateHandle
import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaViewModel
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
interface SaveStateHandler<S : EmaDataState, N : EmaNavigationEvent> {
    fun onSaveStateHandling(
        scope: CoroutineScope,
        saveStateHandle: SavedStateHandle,
        viewModel: EmaViewModel<S, N>
    )
}

fun <S : EmaDataState, N : EmaNavigationEvent> emaSaveStateHandler(
    onSaveStateHandling: (CoroutineScope, SavedStateHandle, EmaViewModel<S, N>) -> Unit
): SaveStateHandler<S, N> {
    return object : SaveStateHandler<S, N> {
        override fun onSaveStateHandling(
            scope: CoroutineScope,
            saveStateHandle: SavedStateHandle,
            viewModel: EmaViewModel<S, N>
        ) {
            onSaveStateHandling.invoke(scope, saveStateHandle, viewModel)
        }

    }
}
