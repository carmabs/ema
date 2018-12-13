package com.carmabs.ema.android.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carmabs.ema.core.concurrency.ConcurrencyManager
import com.carmabs.ema.core.concurrency.DefaultConcurrencyManager
import com.carmabs.ema.core.navigator.EmaNavigationState
import com.carmabs.ema.core.state.EmaBaseState
import com.carmabs.ema.core.state.EmaExtraData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job


/**
 *
 * Abstract base class for ViewModel in MVVM architecture.
 *
 * @param S is template about the class which will represent the state of the view. It has to implement
 * the [EmaBaseState] interface
 *
 * @param NS is the template about the available navigation states contained in the [EmaNavigator] used
 * for the feature that implement this ViewModel
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */
abstract class EmaBaseViewModel<S : EmaBaseState, NS : EmaNavigationState> : ViewModel() {

    /**
     * Observable state that launch event every time a value is set. This value will be the state
     * of the view. When the ViewModel is attached to an observer, if this value is already set up,
     * it will be notified to the new observer
     */
    internal val observableState: MutableLiveData<S> = MutableLiveData()

    /**
     * Observable state that launch event every time a value is set. This value will be a [EmaExtraData]
     * object that can contain any type of object. It will be used for
     * events that only has to be notified once to its observers, e.g: A toast message.
     */
    internal val singleObservableState: SingleLiveEvent<EmaExtraData> = SingleLiveEvent()

    /**
     * Observable state that launch event every time a value is set. [NS] value be will a [EmaNavigationState]
     * object that represent the destination. This observable will be used for
     * events that only has to be notified once to its observers and is used to notify the navigation
     * events
     */
    internal val navigationState: SingleLiveEvent<NS> = SingleLiveEvent()

    /**
     * Manager to handle the threads where the background tasks are going to be launched
     */
    protected var concurrencyManager: ConcurrencyManager = DefaultConcurrencyManager()

    /**
     * The state of the view.
     */
    protected var state: S? = null

    /**
     * Methos called the first time ViewModel is created
     * @param inputState
     */
    open fun onStart(inputState: S? = null) {
        if (state == null) {
            state = inputState ?: createInitialState()
            updateView(state)
        }
    }

    /**
     * Method used to update the state of the view. It will be notified to the observers
     * @param state Tee current state of the view
     */
    protected fun updateView(state: S?) {
        if (state != null)
            this.observableState.value = state
    }

    /**
     * Method used to notify to the observer for a single event that will be notified only once time.
     * It a new observer is attached, it will not be notified
     */
    protected fun sendSingleEvent(extraData: EmaExtraData) {
        this.singleObservableState.value = extraData
    }

    /**
     * Method use to notify a navigation event
     * @param navigation The object that represent the destination of the navigation
     */
    protected fun navigate(navigation: NS) {
        this.navigationState.value = navigation
    }

    /**
     * Methods which must implement the default state of the view
     * @return the default state of the view
     */
    protected abstract fun createInitialState(): S

    /**
     * When a background task must be executed for data retrieving or other background job, it must
     * be called through this method with [block] function
     * @param block is the function that will be executed in background
     * @return the job that can handle the lifecycle of the background task
     */
    protected fun executeUseCase(block: suspend CoroutineScope.() -> Unit): Job {
        return concurrencyManager.launch(block)
    }


    /**
     * Method called when the ViewModel is destroyed. It cancell all background pending tasks
     */
    override fun onCleared() {
        concurrencyManager.cancelPendingTasks()
        super.onCleared()
    }
}