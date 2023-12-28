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
interface EmaViewModel<S : EmaDataState, N : EmaNavigationEvent> {


    val initialState: EmaState<S,N>

    /**
     * Used to know if subscribed view should render the state
     */
    val shouldRenderState:Boolean

    fun setScope(scope: CoroutineScope)

    fun onCreated(initializer: EmaInitializer? = null)

    fun onStartView()

    fun onResumeView()

    fun onPauseView()

    fun onStopView()

    fun subscribeStateUpdates(): Flow<EmaState<S,N>>

    /**
     * Get navigation state as LiveData to avoid state setting from the view
     */
    fun subscribeToNavigationEvents(): Flow<EmaNavigationDirectionEvent>

    /**
     * Get single state as LiveData to avoid state setting from the view
     */
    fun subscribeToSingleEvents(): Flow<EmaEvent>

    fun consumeSingleEvent()

    fun notifyOnNavigated()

    fun onActionBackHardwarePressed()


    val id: String
        get() {
            return "EmaViewModel ID: ${this.javaClass.name}"
        }
}