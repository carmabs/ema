package com.carmabs.ema.android.savestate

import androidx.lifecycle.SavedStateHandle
import com.carmabs.ema.core.initializer.EmaInitializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Created by Carlos Mateo Benito on 16/2/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.comm”>Carlos Mateo Benito</a>
 */
inline fun <reified I : EmaInitializer> serializerSaveStateHandler() = object :
    InitializerRepository {

    override fun save(
        arcInitializer: EmaInitializer,
        savedStateHandle: SavedStateHandle
    ) {
        val initializer: I = arcInitializer as I
        val json = Json.encodeToString(initializer)
        savedStateHandle[EmaInitializer.KEY] = json
    }

    override fun restore(savedStateHandle: SavedStateHandle): EmaInitializer {
        val json: String = savedStateHandle[EmaInitializer.KEY]!!
        return Json.decodeFromString<I>(json)
    }
}