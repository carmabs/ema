package com.carmabs.emax.middleware.result

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.action.ResultEmaAction
import com.carmabs.ema.core.extension.ResultId
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaReceiverModel
import com.carmabs.ema.core.viewmodel.EmaResultHandler
import com.carmabs.emax.middleware.common.EmaxMiddleware
import com.carmabs.emax.middleware.common.EmaNextMiddleware
import com.carmabs.emax.middleware.common.EmaNextMiddlewareResult
import com.carmabs.emax.middleware.common.MiddlewareScope
import com.carmabs.emax.store.EmaxStore

/**
 * Created by Carlos Mateo Benito on 1/10/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */

class ResultEventEmaxMiddleware<S : EmaDataState> internal constructor(
    private val store: EmaxStore<S>,
    resultId: ResultId,
    ownerId: String,
    private val onResultAction: MiddlewareScope<S>.(resultAction: ResultEmaAction) -> EmaAction
) : EmaxMiddleware<S> {

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