package com.carmabs.ema.compose.action

import androidx.compose.runtime.Immutable
import com.carmabs.ema.core.action.FeatureEmaAction
import com.carmabs.ema.core.action.EmaActionDispatcher

/**
 * Created by Carlos Mateo Benito on 16/7/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * Variation of EmaActionDispatcher to improve composables recomposition performance.
 * Make it immutable, guarantees that listeners based on actions are skippables so recomposition is avoided.
 * Otherwise recomposition is launched everytime a listener has inside its implementation and outer params considered
 * unstable
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
@Immutable
interface EmaImmutableActionDispatcher<A : FeatureEmaAction> : EmaActionDispatcher<A>

fun <A : FeatureEmaAction> EmaActionDispatcher<A>.toImmutable():EmaImmutableActionDispatcher<A> {
    return object :EmaImmutableActionDispatcher<A>{
        override fun onAction(action: A) {
            this@toImmutable.onAction(action)
        }
    }
}