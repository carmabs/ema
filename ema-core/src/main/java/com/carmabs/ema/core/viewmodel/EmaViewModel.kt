package com.carmabs.ema.core.viewmodel

import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.model.EmaEvent
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.navigator.EmaNavigationDirectionEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

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

    fun onCleared()

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

    object EMPTY : EmaViewModel<EmaDataState.EMPTY, EmaNavigationEvent.EMPTY> {


        override val initialState: EmaState<EmaDataState.EMPTY,EmaNavigationEvent.EMPTY> = EmaState.Normal(EmaDataState.EMPTY)
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

        }

        override fun onStopView() {

        }

        override fun onCleared() {

        }

        override fun subscribeStateUpdates(): Flow<EmaState<EmaDataState.EMPTY,EmaNavigationEvent.EMPTY>> {
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

        }
    }
}