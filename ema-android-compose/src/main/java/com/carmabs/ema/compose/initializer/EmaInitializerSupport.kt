package com.carmabs.ema.compose.initializer

import com.carmabs.ema.android.initializer.bundle.strategy.BundleSerializerStrategy
import com.carmabs.ema.core.initializer.EmaInitializer
import kotlinx.serialization.KSerializer

/**
 * Created by Carlos Mateo Benito on 20/3/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
open class EmaInitializerSupport(
    val serializerStrategy: BundleSerializerStrategy,
    val overrideInitializer: EmaInitializer? = null
) {
    companion object {
        fun <I : EmaInitializer> kSerialization(
            serializer: KSerializer<I>,
            overrideInitializer: I? = null
        ) = EmaInitializerSupport(
            BundleSerializerStrategy.kSerialization(serializer),
            overrideInitializer
        )
    }

}
