package com.carmabs.ema.core.viewmodel.emux.middleware

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.emux.store.EmaStore
import com.carmabs.ema.core.viewmodel.emux.SampleDataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by Carlos Mateo Benito on 29/9/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
typealias EmaNext = (EmaAction) -> Unit

interface EmaMiddleware<S : EmaDataState> {

    context(EmaMiddlewareScope)
    operator fun invoke(
        store: EmaStore<S>,
        action: EmaAction
    ): EmaNext

}

private class Sample : EmaMiddleware<SampleDataState> {
    context(EmaMiddlewareScope)
    override fun invoke(
        store: EmaStore<SampleDataState>,
        action: EmaAction
    ): EmaNext {
        return next(action)
    }


}

class EmaMiddlewareScope(
    private val scope: CoroutineScope,
    private val nextAction: (EmaAction) -> EmaNext
) {

    fun next(action: EmaAction): EmaNext {
        return nextAction.invoke(action)
    }

    fun sideEffect(
        effect: suspend () -> (EmaAction) -> Unit
    ): (EmaAction) -> Unit {
        return {
            scope.launch {
                effect.invoke()
            }
        }
    }
}