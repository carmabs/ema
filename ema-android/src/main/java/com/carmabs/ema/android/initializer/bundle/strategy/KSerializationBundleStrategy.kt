package com.carmabs.ema.android.initializer.bundle.strategy

import android.os.Bundle
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
class KSerializationBundleStrategy<I:EmaInitializer>(private val kSerializer: KSerializer<I>):BundleSerializerStrategy{
    override fun save(initializer:EmaInitializer,bundle: Bundle){
        val json = toStringValue(initializer)
        bundle.putString(EmaInitializer.KEY, json)
    }

    override fun toStringValue(initializer:EmaInitializer): String {
        return Json.encodeToString(kSerializer, initializer as I)
    }

    override fun fromStringValue(value: String): EmaInitializer {
        return  Json.decodeFromString(kSerializer, value)
    }

    override fun restore(bundle: Bundle):EmaInitializer?{
        val json = bundle.getString(EmaInitializer.KEY)
        return json?.let { Json.decodeFromString(kSerializer, it) }
    }
}