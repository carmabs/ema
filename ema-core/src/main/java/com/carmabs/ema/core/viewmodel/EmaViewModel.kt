package com.carmabs.ema.core.viewmodel

import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.model.EmaEvent
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.navigator.EmaNavigationDirectionEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * View model to handle view states.
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
interface EmaViewModel<S : EmaDataState, D : EmaNavigationEvent>{


    val initialState:EmaState<S>

   fun setScope(scope: CoroutineScope)

    fun onStart(initializer: EmaInitializer? = null, startedFinishListener: (() -> Unit)? = null)

   fun onResumeView()

   fun onPauseView()

    fun onStopView()

    fun subscribeStateUpdates(): Flow<EmaState<S>>

    /**
     * Get navigation state as LiveData to avoid state setting from the view
     */
    fun subscribeToNavigationEvents(): Flow<EmaNavigationDirectionEvent>

    /**
     * Get single state as LiveData to avoid state setting from the view
     */
    fun subscribeToSingleEvents(): Flow<EmaEvent>

    fun consumeSingleEvent()

    fun consumeNavigation()

    /**
     * Override and implement this to setup listener that is  called when physic back button is pressed
     * @return True if you want the back pressed default behaviour is launched. False otherwise.
     */
    val onBackHardwarePressedListener: (() -> Boolean)?


    val id: String
        get() {
            return "EmaViewModel ID: ${this.javaClass.name}"
        }
}