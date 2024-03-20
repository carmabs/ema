package com.carmabs.ema.android.initializer.savestate

import androidx.lifecycle.SavedStateHandle
import com.carmabs.ema.android.initializer.bundle.strategy.SaveStateSerializerStrategy
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.initializer.EmaInitializerSerializer

/**
 * Created by Carlos Mateo Benito on 19/3/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class SaveStateSerializer(
    private val savedStateHandle: SavedStateHandle,
    private val strategy: SaveStateSerializerStrategy
) : EmaInitializerSerializer {
    override fun save(initializer: EmaInitializer) {
        strategy.save(initializer, savedStateHandle)
    }

    override fun restore(): EmaInitializer? {
        return strategy.restore(savedStateHandle)
    }
}