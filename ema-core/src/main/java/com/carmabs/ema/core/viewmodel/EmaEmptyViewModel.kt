package com.carmabs.ema.core.viewmodel

import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.model.EmaEvent
import com.carmabs.ema.core.navigator.EmaEmptyNavigationEvent
import com.carmabs.ema.core.navigator.EmaNavigationDirectionEvent
import com.carmabs.ema.core.state.EmaEmptyState
import com.carmabs.ema.core.state.EmaState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Created by Carlos Mateo Benito on 21/7/22.
 *
 * <p>
 * Copyright (c) 2022 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
object EmaEmptyViewModel : EmaViewModel<EmaEmptyState, EmaEmptyNavigationEvent> {


    override val initialState: EmaState<EmaEmptyState,EmaEmptyNavigationEvent> = EmaState.Normal(EmaEmptyState)
    override val shouldRenderState: Boolean
        get() = true

    override fun setScope(scope: CoroutineScope) {

    }

    override fun onCreated(initializer: EmaInitializer?) {

    }

    override fun onStartView() {

    }

    override fun onResumeView() {

    }

    override fun onPauseView() {
        TODO("Not yet implemented")
    }

    override fun onStopView() {

    }

    override fun subscribeStateUpdates(): Flow<EmaState<EmaEmptyState,EmaEmptyNavigationEvent>> {
        return emptyFlow()
    }

    override fun subscribeToNavigationEvents(): Flow<EmaNavigationDirectionEvent> {
        return emptyFlow()
    }

    override fun subscribeToSingleEvents(): Flow<EmaEvent> {
        return emptyFlow()
    }

    override fun consumeSingleEvent() {

    }

    override fun notifyOnNavigated() {

    }

    override fun onActionBackHardwarePressed() {
        TODO("Not yet implemented")
    }
}