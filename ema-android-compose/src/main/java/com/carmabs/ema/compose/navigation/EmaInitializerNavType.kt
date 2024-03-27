package com.carmabs.ema.compose.navigation

import android.os.Bundle
import androidx.navigation.NavType
import com.carmabs.ema.android.initializer.bundle.strategy.BundleSerializerStrategy
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
internal class EmaInitializerNavType(
    private val serializerStrategy: BundleSerializerStrategy
) : NavType<EmaInitializer>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): EmaInitializer? {
        return serializerStrategy.restore(bundle)
    }

    override fun parseValue(value: String): EmaInitializer {
        return serializerStrategy.fromStringValue(value)
    }

    override fun serializeAsValue(value: EmaInitializer): String {
        return serializerStrategy.toStringValue(value)
    }

    override fun put(bundle: Bundle, key: String, value: EmaInitializer) {
        serializerStrategy.save(value,bundle)
    }
}