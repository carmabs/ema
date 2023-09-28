package com.carmabs.ema.core.viewmodel.emux.middleware

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.action.ResultEmaAction
import com.carmabs.ema.core.extension.ResultId
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaReceiverModel
import com.carmabs.ema.core.viewmodel.EmaResultHandler
import com.carmabs.ema.core.viewmodel.emux.store.EmaStore

/**
 * Created by Carlos Mateo Benito on 1/10/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */

class ResultEventEmaMiddleware<S : EmaDataState, D : EmaNavigationEvent> internal constructor(
    private val store: EmaStore<S>,
    private val resultId: ResultId,
    private val ownerId: String,
    private val onResultAction: EmaMiddlewareScope.(resultAction: ResultEmaAction) -> EmaNext
) : EmaMiddleware<S> {

    private val resultHandler: EmaResultHandler = EmaResultHandler.getInstance()

    init {
        resultHandler.addResultReceiver(
            EmaReceiverModel(
                resultId.id,
                ownerId
            ) {
                store.dispatch(ResultEmaAction(it))
            }
        )
    }

    context(EmaMiddlewareScope)
    override fun invoke(
        store: EmaStore<S>,
        action: EmaAction,
    ): EmaNext {
        return when (action) {
            is ResultEmaAction -> {
                {
                    onResultAction.invoke(
                        this@EmaMiddlewareScope,
                        action
                    )
                }
            }

            else ->
                next(action)
        }
    }
}