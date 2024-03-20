package com.carmabs.ema.android.initializer.bundle.strategy

import androidx.lifecycle.SavedStateHandle
import com.carmabs.ema.core.initializer.EmaInitializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

/**
 * Created by Carlos Mateo Benito on 19/3/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class KSerializationSaveStateStrategy<I:EmaInitializer>(private val kSerializer: KSerializer<I>):SaveStateSerializerStrategy{
    override fun save(initializer:EmaInitializer,savedStateHandle: SavedStateHandle){
        val json = Json.encodeToString(kSerializer, initializer as I)
        savedStateHandle[EmaInitializer.KEY] = json
    }

    override fun restore(savedStateHandle: SavedStateHandle):EmaInitializer?{
        val json = savedStateHandle.get<String>(EmaInitializer.KEY)
        return json?.let { Json.decodeFromString(kSerializer, it) }
    }
}