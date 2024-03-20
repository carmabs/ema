package com.carmabs.ema.android.initializer.bundle

import android.os.Bundle
import com.carmabs.ema.android.initializer.bundle.strategy.BundleSerializerStrategy
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
class BundleSerializer(
    private val bundle: Bundle,
    private val strategy: BundleSerializerStrategy
) : EmaInitializerSerializer {
    override fun save(initializer: EmaInitializer) {
        strategy.save(initializer, bundle)
    }

    override fun restore(): EmaInitializer? {
        return strategy.restore(bundle)
    }
}