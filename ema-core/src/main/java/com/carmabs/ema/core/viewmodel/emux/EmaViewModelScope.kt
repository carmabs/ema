package com.carmabs.ema.core.viewmodel.emux

import com.carmabs.ema.core.extension.resultId
import com.carmabs.ema.core.model.EmaEvent
import com.carmabs.ema.core.navigator.EmaNavigationDirection
import com.carmabs.ema.core.navigator.EmaNavigationDirectionEvent
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.viewmodel.EmaResultHandler
import com.carmabs.ema.core.viewmodel.EmaResultModel
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Created by Carlos Mateo Benito on 29/9/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class EmaViewModelScope<in D : EmaNavigationEvent> internal constructor(
    private val resultHandler: EmaResultHandler,
    private val viewModelId:String,
    private val navigationState: MutableSharedFlow<EmaNavigationDirectionEvent>,
    private val observableSingleEvent: MutableSharedFlow<EmaEvent>
) {
    /**
     * Method use to notify a navigation event
     * @param navigation The object that represent the destination of the navigation
     */
    fun navigate(navigation: D) {
        navigationState.tryEmit(
            EmaNavigationDirectionEvent.Launched(
                EmaNavigationDirection.Forward(
                    navigation
                )
            )
        )
    }

    fun addResult(data: Any?, resultId: String? = null) {
        resultHandler.addResult(
            EmaResultModel(
                code = EmaViewModelReducer::class.resultId(resultId).id,
                ownerId = viewModelId,
                data = data
            )
        )
    }

    /**
     * Method use to notify a navigation back event
     */
    fun navigateBack() {
        navigationState.tryEmit(EmaNavigationDirectionEvent.Launched(EmaNavigationDirection.Back))

    }

    /**
     * Method used to notify to the observer for a single event that will be notified only once time.
     * It a new observer is attached, it will not be notified
     */
    fun singleEvent(extraData: EmaExtraData) {
        observableSingleEvent.tryEmit(EmaEvent.Launched(extraData))
    }

}