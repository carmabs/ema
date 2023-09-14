package com.carmabs.ema.core.viewmodel

import com.carmabs.ema.core.concurrency.EmaMainScope
import com.carmabs.ema.core.constants.INT_ONE
import com.carmabs.ema.core.extension.ResultId
import com.carmabs.ema.core.extension.resultId
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.model.EmaFunctionResultHandler
import com.carmabs.ema.core.model.emaFlowSingleEvent
import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * View model to handle view states.
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
abstract class EmaViewModel<S : EmaDataState, D : EmaDestination>(
    initialDataState: S,
    defaultScope: CoroutineScope = EmaMainScope()
) {


    /**
     * The scope where coroutines will be launched by default.
     */
    protected var scope: CoroutineScope = defaultScope
        private set

    internal fun setScope(scope: CoroutineScope) {
        this.scope = scope
    }


    private val pendingEvents = mutableListOf<() -> Unit>()

    /**
     * Observable state that launch event every time a value is set. This value will be the state
     * of the view. When the ViewModel is attached to an observer, if this value is already set up,
     * it will be notified to the new observer. Could be different from state if some changes of the
     * current state has not been notified to the view (Ex: a switch has been changed and the state has
     * been modified, but we don't want no notify to the view to avoid infinite loop ->
     *  switch modified
     *      -> switch state saved on view model if there is view recreation
     *          -> it is notified to the view
     *              -> switch has been set again
     *                  -> saved in view model ------> INFINITE LOOP)
     */
    private val observableState: MutableSharedFlow<EmaState<S>> = MutableSharedFlow(
        replay = INT_ONE,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    /**
     * Observable state that launch event every time a value is set. This value will be a [EmaExtraData]
     * object that can contain any type of object. It will be used for
     * events that only has to be notified once to its observers, e.g: A toast message.
     */
    private val singleObservableState: MutableSharedFlow<EmaExtraData> = emaFlowSingleEvent()

    /**
     * Observable state that launch event every time a value is set. [D] value be will a [EmaDestination]
     * object that represent the destination. This observable will be used for
     * events that only has to be notified once to its observers and is used to notify the navigation
     * events
     */
    private val navigationState: MutableSharedFlow<D?> = MutableSharedFlow(
        replay = INT_ONE,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    /**
     * Determine if viewmodel is first time resumed
     *
     */
    private var firstTimeResumed: Boolean = true

    /**
     * To determine if the view must be updated when view model is created automatically
     */
    protected open val updateOnInitialization: Boolean = true

    /**
     * Determine if the viewmodel has initialized its state
     */

    protected var hasBeenInitialized: Boolean = false
        private set

    /**
     * Override and implement this to setup listener that is  called when physic back button is pressed
     * @return True if you want the back pressed default behaviour is launched. False otherwise.
     */
    open val onBackHardwarePressedListener: (() -> Boolean)? = null


    /**
     * Methods called the first time ViewModel is created
     * @param initializer
     * @param startedFinishListener: (() -> Unit) listener when starting has been finished
     */
    fun onStart(initializer: EmaInitializer? = null, startedFinishListener: (() -> Unit)? = null) {
        if (!hasBeenInitialized) {
            if (!normalContentData.checkIsValidStateDataClass()) {
                throw java.lang.IllegalStateException("The EmaDataState class must be a data class")
            }
            hasBeenInitialized = true
            onResultListenerSetup()
            if (updateOnInitialization)
                observableState.tryEmit(state)
            onStateCreated(initializer)
            onViewStarted()
            startedFinishListener?.invoke()
        } else {
            //We call this to update the data if it has been not be emitted
            // if last time was updated by updateDataState
            observableState.tryEmit(state)
            onViewStarted()
            startedFinishListener?.invoke()
        }
    }

    /**
     * Call on first time view model is initialized
     */
    abstract fun onStateCreated(initializer: EmaInitializer? = null)

    /**
     * Called when view is shown in foreground
     */
    fun onResumeView() {
        onViewResumed()
        firstTimeResumed = false
    }

    /**
     * Called when view is hidden in background
     */
    fun onPauseView() {
        onViewPaused()
    }

    fun onStopView() {
        onViewStopped()
    }

    /**
     * Called always the view goes to the foreground
     */
    protected open fun onViewResumed() = Unit

    /**
     * Called always the view is not fully visible
     */
    protected open fun onViewPaused() = Unit

    /**
     * Called when the view is visible to the user
     */
    protected open fun onViewStarted() = Unit

    /**
     * Called always the view goes to the background
     */
    protected open fun onViewStopped() = Unit


    /**
     * Get observable state as LiveDaya to avoid state setting from the view
     */
    fun getObservableState(): SharedFlow<EmaState<S>> = observableState

    /**
     * Get current state of view
     */
    protected fun getCurrentState(): EmaState<S> = state

    /**
     * Get navigation state as LiveData to avoid state setting from the view
     */
    fun getNavigationState(): SharedFlow<D?> = navigationState

    /**
     * Get single state as LiveData to avoid state setting from the view
     */
    fun getSingleObservableState(): SharedFlow<EmaExtraData> = singleObservableState


    /**
     * Method used to update the state of the view. It will be notified to the observers
     * @param state Tee current state of the view
     */
    private fun updateView(state: EmaState<S>) {
        this.state = state
        observableState.tryEmit(state)
    }

    /**
     * Method used to notify to the observer for a single event that will be notified only once time.
     * It a new observer is attached, it will not be notified
     */
    protected fun notifySingleEvent(extraData: EmaExtraData) {
        singleObservableState.tryEmit(extraData)
    }

    /**
     * Method use to notify a navigation event
     * @param navigation The object that represent the destination of the navigation
     */
    protected fun navigate(navigation: D) {
        navigation.resetNavigated()
        navigationState.tryEmit(navigation)
    }

    /**
     * Method use to notify a navigation back event
     */
    protected fun navigateBack() {
        navigationState.tryEmit(null)
    }

    /**
     * When a background task must be executed for data retrieving or other background job, it must
     * be called through this method with [action] function
     * @param action is the function that will be executed in background
     * @param dispatcher where the useCase is launched by default
     * @return The EmaUseCaseResult where you can handle the result with the methods
     * - onSuccess when the result of action function is successful
     * - onError when the action function has thrown an error
     * - onFinish when the action function has ended, independently if an error has been thrown
     * - job returns the job where the action function has been executed
     */
    protected fun <T> executeUseCase(
        dispatcher: CoroutineContext = this.scope.coroutineContext,
        action: suspend CoroutineScope.() -> T
    ): EmaFunctionResultHandler<T> {
        return EmaFunctionResultHandler(scope, dispatcher, action)
    }

    /**
     * When a background task must be executed for data retrieving or other background job, it must
     * be called through this method with [block] function
     * @param dispatcher where the useCase is launched by default
     * @param block is the function that will be executed in background
     * @return the job that can handle the lifecycle of the background task
     */
    protected fun runSuspend(
        dispatcher: CoroutineContext = scope.coroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return scope.launch(
            dispatcher,
            block = block
        )
    }

    /**
     * Method to override onCleared ViewModel method
     */
    protected open fun onDestroy() = Unit

    /**
     * Normal state content of the view
     */
    private var normalContentData: S = initialDataState

    val initialState = EmaState.Normal(initialDataState)
    /**
     * The state of the view.
     */
    internal var state: EmaState<S> = initialState
        private set

    private val emaResultHandler: EmaResultHandler = EmaResultHandler.getInstance()


    /**
     * Here should implement the listener for result data from other views through [addOnResultListener] method
     */
    protected open fun onResultListenerSetup() = Unit


    /**
     * Update the data of the state without notifying it to the view.
     */
    private fun updateData(newState: S): EmaState<S> {
        return when (state) {
            is EmaState.Normal -> {
                EmaState.Normal(newState)
            }

            is EmaState.Overlapped -> {
                val alternativeState = state as EmaState.Overlapped
                EmaState.Overlapped(newState, alternativeState.dataOverlapped)
            }
        }
    }

    /**
     * Update the current state and update the normal view state by default
     * @param changeStateFunction create the new state
     */
    protected fun updateToNormalState(changeStateFunction: S.() -> S) {
        normalContentData = changeStateFunction.invoke(normalContentData)
        state = EmaState.Normal(normalContentData)
        updateToNormalState()
    }

    /**
     * Used for trigger an update on the view
     * Use the EmaState -> Normal
     */
    protected fun updateToNormalState() {
        updateView(EmaState.Normal(normalContentData))
    }

    /**
     * Update the data of current state without notify it to the view.
     * @param changeStateFunction create the new state
     */
    protected fun updateDataState(changeStateFunction: S.() -> S) {
        normalContentData = changeStateFunction.invoke(normalContentData)
        state = updateData(normalContentData)
    }


    /**
     * Get the current view state
     * @return the current viewState or null if it has not been initialized
     */
    @Deprecated(
        "Use stateData instead. This method will be deleted in future.",
        replaceWith = ReplaceWith("stateData")
    )
    protected fun getDataState(): S {
        return normalContentData
    }

    protected val stateData: S
        get() = normalContentData


    /**
     * Used for trigger an updateOverlayedState event on the view
     * Use the EmaState -> Alternative
     * @param data with updateOverlayedState information
     */
    protected fun updateToOverlappedState(data: EmaExtraData? = null) {
        val overlappedData: EmaState.Overlapped<S> = data?.let {
            EmaState.Overlapped(normalContentData, dataOverlapped = it)
        } ?: EmaState.Overlapped(normalContentData)
        updateView(overlappedData)
    }

    /**
     * Set a result for previous view when the current one is destroyed
     */
    protected fun addResult(data: Any?, resultId: String? = null) {
        emaResultHandler.addResult(
            EmaResultModel(
                code = this::class.resultId(resultId).id,
                ownerId = id,
                data = data
            )
        )
    }

    /**
     * Set the listener for back data when the result view is destroyed. To select the resultId use the EmaViewModel::class.resultId() method
     * of the selected implementation of EmaViewModel whose result is required. Example SampleEmaViewModel::class.resultId()
     */
    protected fun addOnResultListener(
        resultId: ResultId,
        receiver: (Any?) -> Unit
    ) {
        emaResultHandler.addResultReceiver(
            EmaReceiverModel(
                resultCode = resultId.id,
                ownerId = id,
                function = receiver
            )
        )
    }

    /**
     * Method called when the ViewModel is destroyed. It cancels all background pending tasks.
     * Check call name for EmaAndroidView. It uses reflection to call this internal method
     */
    internal fun onCleared() {
        emaResultHandler.notifyResults(id)
        emaResultHandler.removeResultListener(id)
        scope.cancel()
        onDestroy()
    }

    val id: String
        get() {
            return "EmaViewModel ID: ${this.javaClass.name}"
        }
}