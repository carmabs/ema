package com.carmabs.ema.android.initializer.savestate

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import com.carmabs.ema.android.initializer.bundle.strategy.BundleSerializerStrategy
import com.carmabs.ema.android.initializer.bundle.strategy.SaveStateSerializerStrategy
import com.carmabs.ema.core.initializer.EmaInitializer
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
abstract class SaveStateManager<S : EmaDataState, N : EmaNavigationEvent>(private val strategy: BundleSerializerStrategy) :
    SaveStateHandler<S, N> {
    fun save(initializer: EmaInitializer, savedStateHandle: SavedStateHandle) {
        val bundle = Bundle()
        strategy.save(initializer, bundle)
        savedStateHandle[EmaInitializer.KEY] = bundle
    }

    fun restore(savedStateHandle: SavedStateHandle): EmaInitializer? {
        val bundle = savedStateHandle.get<Bundle>(EmaInitializer.KEY)
        return bundle?.let {  strategy.restore(bundle) }
    }
}

fun <S : EmaDataState, N : EmaNavigationEvent> emaSaveStateManagerOf(
    serializer: BundleSerializerStrategy,
    saveStateHandler: SaveStateHandler<S, N>? = null
): SaveStateManager<S, N> {
    return object : SaveStateManager<S, N>(serializer) {
        override fun onSaveStateHandling(
            scope: CoroutineScope,
            saveStateHandle: SavedStateHandle,
            viewModel: EmaViewModel<S, N>
        ) {
            saveStateHandler?.onSaveStateHandling(scope, saveStateHandle, viewModel)
        }
    }
}
