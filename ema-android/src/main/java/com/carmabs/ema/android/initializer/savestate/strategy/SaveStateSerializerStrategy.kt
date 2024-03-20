package com.carmabs.ema.android.initializer.bundle.strategy

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import com.carmabs.ema.core.initializer.EmaInitializer

/**
 * Created by Carlos Mateo Benito on 19/3/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
interface SaveStateSerializerStrategy {
    fun save(initializer: EmaInitializer, savedStateHandle: SavedStateHandle)

    fun restore(savedStateHandle: SavedStateHandle): EmaInitializer?

    companion object {
        val EMPTY = object : SaveStateSerializerStrategy {
            override fun save(initializer: EmaInitializer, savedStateHandle: SavedStateHandle) = Unit

            override fun restore(savedStateHandle: SavedStateHandle): EmaInitializer? = null
        }
    }
}