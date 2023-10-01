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


    override val initialState: EmaState<EmaEmptyState> = EmaState.Normal(EmaEmptyState)

    override fun setScope(scope: CoroutineScope) {

    }

    override fun onStart(initializer: EmaInitializer?, startedFinishListener: (() -> Unit)?) {

    }

    override fun onResumeView() {

    }

    override fun onPauseView() {
        TODO("Not yet implemented")
    }

    override fun onStopView() {

    }

    override fun subscribeStateUpdates(): Flow<EmaState<EmaEmptyState>> {
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

    override fun consumeNavigation() {

    }

    override val onBackHardwarePressedListener: (() -> Boolean)? = null
}