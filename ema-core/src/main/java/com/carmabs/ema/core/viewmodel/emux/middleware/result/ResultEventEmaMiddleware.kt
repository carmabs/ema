package com.carmabs.ema.core.viewmodel.emux.middleware.result

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.action.ResultEmaAction
import com.carmabs.ema.core.extension.ResultId
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaReceiverModel
import com.carmabs.ema.core.viewmodel.EmaResultHandler
import com.carmabs.ema.core.viewmodel.emux.middleware.common.EmaMiddleware
import com.carmabs.ema.core.viewmodel.emux.middleware.common.EmaNextMiddleware
import com.carmabs.ema.core.viewmodel.emux.middleware.common.EmaNextMiddlewareResult
import com.carmabs.ema.core.viewmodel.emux.middleware.common.MiddlewareScope
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

class ResultEventEmaMiddleware<S : EmaDataState> internal constructor(
    private val store: EmaStore<S>,
    resultId: ResultId,
    ownerId: String,
    private val onResultAction: MiddlewareScope<S>.(resultAction: ResultEmaAction) -> EmaAction
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

    context(MiddlewareScope<S>)
    override fun invoke(
        action: EmaAction,
        next: EmaNextMiddleware
    ): EmaNextMiddlewareResult {
        return when (action) {
            is ResultEmaAction -> {
                next(onResultAction.invoke(
                    this@MiddlewareScope,
                    action
                ))
            }
            else ->
                next(action)
        }
    }
}