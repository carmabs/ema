package com.carmabs.ema.android.initializer

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
data class EmaInitializerBundle(
    val initializer: EmaInitializer,
    val serializer: BundleSerializerStrategy
)