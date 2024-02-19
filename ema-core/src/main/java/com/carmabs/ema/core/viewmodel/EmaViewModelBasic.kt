package com.carmabs.ema.core.viewmodel

import com.carmabs.ema.core.concurrency.EmaMainScope
import com.carmabs.ema.core.constants.INT_ONE
import com.carmabs.ema.core.extension.ResultId
import com.carmabs.ema.core.extension.distinctNavigationChanges
import com.carmabs.ema.core.extension.distinctSingleEventChanges
import com.carmabs.ema.core.extension.resultId
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.model.EmaEvent
import com.carmabs.ema.core.model.EmaFunctionResultHandler
import com.carmabs.ema.core.navigator.EmaNavigationDirectionEvent
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.coroutines.CoroutineContext

/**
 * View model to handle view states.
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
abstract class EmaViewModelBasic<S : EmaDataState, N : EmaNavigationEvent>(
    initialDataState: S,
    defaultScope: CoroutineScope = EmaMainScope()
) : EmaViewModel<S, N> {

    private val singleSideEffectMap by lazy {
        hashMapOf<String,Job>()
    }
    /**
     * The scope where coroutines will be launched by default.
     */
    protected var scope: CoroutineScope = defaultScope
        private set


     /**
     * To determine if the view must be updated when view model is created automatically
     */
    protected open val updateOnInitialization: Boolean = true

    /**
     * Used to know if state has been updated at least once
     */
    private var hasBeenUpdated = false

    /**
     * Used to know if subscribed view should render the state
     */
    override val shouldRenderState: Boolean
        get() {
            return updateOnInitialization || hasBeenUpdated
        }

    override fun setScope(scope: CoroutineScope) {
        this.scope = scope
    }

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
    private val eventObservableState: MutableSharedFlow<EmaState<S, N>> = MutableSharedFlow(
        replay = INT_ONE,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )


    /**
     * Observable for state data update. It must be a separate one to allow separate update on updateToNormalState(). Otherwise, if eventObservableState was used,
     * if a user make a modifyState, then sends a singleEvent or navigation, the observableState was launch to notify data because their data are different.
     */
    private val dataObservableState: MutableSharedFlow<EmaState<S, N>> = MutableSharedFlow(
        replay = INT_ONE,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    /**
     * Determine if viewmodel is first time resumed
     *
     */
    private var firstTimeResumed: Boolean = true

    /**
     * Determine if the viewmodel has initialized its state
     */

    protected var hasBeenInitialized: Boolean = false
        private set

    /**
     * Methods called the first time ViewModel is created
     * @param initializer
     */
    final override fun onCreated(initializer: EmaInitializer?) {
        if (!hasBeenInitialized) {
            if (!state.data.checkIsValidStateDataClass()) {
                throw java.lang.IllegalStateException("The EmaDataState class must be a data class")
            }
            hasBeenInitialized = true
            onResultListenerSetup()
            if (updateOnInitialization)
                dataObservableState.tryEmit(state)
            onStateCreated(initializer)
        }
    }

    protected fun setBackResult(result: Any) {
        state = state.setResult(result)
    }

    protected fun clearBackResult() {
        state = state.clearResult()
    }

    override fun onStartView() {
        onViewStarted()
    }

    /**
     * Call on first time view model is initialized
     */
    abstract fun onStateCreated(initializer: EmaInitializer? = null)

    /**
     * Called when view is shown in foreground
     */
    override fun onResumeView() {
        onViewResumed()
        firstTimeResumed = false
    }

    /**
     * Called when view is hidden in background
     */
    override fun onPauseView() {
        onViewPaused()
    }

    override fun onStopView() {
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
    override fun subscribeStateUpdates(): Flow<EmaState<S, N>> = dataObservableState

    /**
     * Get current state of view
     */
    protected fun getCurrentState(): EmaState<S, N> = state

    /**
     * Get navigation state as LiveData to avoid state setting from the view
     */
    override fun subscribeToNavigationEvents(): Flow<EmaNavigationDirectionEvent> =
        eventObservableState.distinctNavigationChanges()


    /**
     * Get single state as LiveData to avoid state setting from the view
     */
    override fun subscribeToSingleEvents(): Flow<EmaEvent> =
        eventObservableState.distinctSingleEventChanges()


    /**
     * Method used to update the state of the view. It will be notified to the observers
     * @param state Tee current state of the view
     */
    private fun updateEventView(state: EmaState<S, N>) {
        hasBeenUpdated = true
        this.state = state
        eventObservableState.tryEmit(state)
    }

    /**
     * Method used to update the state of the view. It will be notified to the observers
     * @param state Tee current state of the view
     */
    private fun updateDataView(state: EmaState<S, N>) {
        hasBeenUpdated = true
        this.state = state
        dataObservableState.tryEmit(state)
    }

    /**
     * Method used to notify to the observer for a single event that will be notified only once time.
     * It a new observer is attached, it will not be notified
     */
    protected open fun notifySingleEvent(extraData: EmaExtraData) {
        updateEventView(state.setSingleEvent(extraData))
    }

    override fun consumeSingleEvent() {
        updateEventView(state.consumeSingleEvent())
    }

    /**
     * Method use to notify a navigation event
     * @param navigation The object that represent the destination of the navigation
     */
    protected fun navigate(navigation: N) {
        updateEventView(state.navigate(navigation))
    }

    override fun notifyOnNavigated() {
        updateEventView(state.onNavigated())
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
    protected fun <T> sideEffect(
        dispatcher: CoroutineContext = this.scope.coroutineContext,
        action: suspend CoroutineScope.() -> T
    ): EmaFunctionResultHandler<T> {
        return EmaFunctionResultHandler(scope, dispatcher, action)
    }

    protected fun <T> singleSideEffect(
        id:String,
        dispatcher: CoroutineContext = this.scope.coroutineContext,
        action: suspend CoroutineScope.() -> T
    ): EmaFunctionResultHandler<T> {
        singleSideEffectMap[id]?.cancel()
        val handler =  EmaFunctionResultHandler(scope, dispatcher, action)
        singleSideEffectMap[id] = handler.job
        return handler
    }

    /**
     * Method to override onCleared ViewModel method
     */
    protected open fun onDestroy() = Unit

    /**
     * Normal state content of the view
     */
    final override val initialState: EmaState<S, N> = EmaState.Normal(initialDataState)

    /**
     * The state of the view.
     */
    internal var state: EmaState<S, N> = initialState
        private set

    private val emaResultHandler: EmaResultHandler = EmaResultHandler.getInstance()


    /**
     * Here should implement the listener for result data from other views through [addOnResultListener] method
     */
    protected open fun onResultListenerSetup() = Unit

    /**
     * Update the current state and update the normal view state by default
     * @param changeStateFunction create the new state
     */
    protected fun updateToNormalState(changeStateFunction: S.() -> S) {
        updateDataView(state.normal {
            changeStateFunction.invoke(this)
        })
    }

    /**
     * Used for trigger an update on the view
     * Use the EmaState -> Normal
     */
    protected fun updateToNormalState() {
        updateDataView(state.normal())
    }

    /**
     * Update the data of current state without notify it to the view.
     * @param changeStateFunction create the new state
     */
    protected fun modifyDataState(changeStateFunction: S.() -> S) {
        state = state.update {
            changeStateFunction.invoke(this)
        }
    }

    protected fun updateState(changeStateFunction: S.() -> S) {
        updateDataView(state.update {
            changeStateFunction(this)
        })
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
        return state.data
    }

    protected val stateData: S
        get() = state.data


    /**
     * Used for trigger an updateOverlayedState event on the view
     * Use the EmaState -> Alternative
     * @param data with updateOverlayedState information
     */
    protected fun updateToOverlappedState(data: EmaExtraData? = null) {
        val overlappedData: EmaState.Overlapped<S, N> = data?.let {
            state.overlapped(extraData = it)
        } ?: state.overlapped()
        updateDataView(overlappedData)
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

    override fun onActionBackHardwarePressed() {
        updateEventView(state.navigateBack())
    }
}