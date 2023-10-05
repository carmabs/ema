package com.carmabs.emax

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.action.FeatureEmaAction
import com.carmabs.ema.core.action.EmaActionDispatcher
import com.carmabs.ema.core.action.ResultEmaAction
import com.carmabs.ema.core.action.ViewEmaAction
import com.carmabs.ema.core.concurrency.EmaMainScope
import com.carmabs.ema.core.constants.INT_ONE
import com.carmabs.ema.core.extension.ResultId
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.initializer.EmaInitializerEmpty
import com.carmabs.ema.core.model.EmaEvent
import com.carmabs.ema.core.navigator.EmaNavigationDirectionEvent
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.core.viewmodel.EmaResultHandler
import com.carmabs.ema.core.viewmodel.EmaViewModel
import com.carmabs.emax.middleware.log.LoggerEmaxMiddleware
import com.carmabs.emax.middleware.common.MiddlewareScope
import com.carmabs.emax.middleware.initializer.InitializerEmaxMiddleware
import com.carmabs.emax.middleware.result.ResultEventEmaxMiddleware
import com.carmabs.emax.middleware.viewevent.ViewEventEmaxMiddleware
import com.carmabs.emax.middleware.viewmodel.ViewModelEmaxMiddleware
import com.carmabs.emax.reducer.EmaFeatureReducerScope
import com.carmabs.emax.reducer.FeatureEmaxReducer
import com.carmabs.emax.store.EmaxStore
import com.carmabs.emax.store.EmaxStoreSetupScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * View model to handle view states.
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
abstract class EmaxViewModel<S : EmaDataState, A : FeatureEmaAction, D : EmaNavigationEvent>(
    initialDataState: S,
    defaultScope: CoroutineScope = EmaMainScope()
) : EmaViewModel<S, D>, EmaActionDispatcher<A> {

    /**
     * The scope where coroutines will be launched by default.
     */
    private var scope: CoroutineScope = defaultScope
        private set

    final override fun setScope(scope: CoroutineScope) {
        this.scope = scope
    }

    final override val initialState = EmaState.Normal(initialDataState)

    private var currentState: EmaState<S> = initialState

    private val emaResultHandler: EmaResultHandler = EmaResultHandler.getInstance()

    /**
     * Observable state that launch event every time a value is set. [D] value be will a [EmaNavigationEvent]
     * object that represent the destination. This observable will be used for
     * events that only has to be notified once to its observers and is used to notify the navigation
     * events
     */
    private val navigationState: MutableSharedFlow<EmaNavigationDirectionEvent> = MutableSharedFlow(
        replay = INT_ONE,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val channelAction = Channel<FeatureEmaAction>()

    private val observableAction = channelAction.receiveAsFlow()


    /**
     * Observable state that launch event every time a value is set. This value will be a [EmaExtraData]
     * object that can contain any type of object. It will be used for
     * events that only has to be notified once to its observers, e.g: A toast message.
     */
    private val observableSingleEvent: MutableSharedFlow<EmaEvent> = MutableSharedFlow(
        replay = INT_ONE,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val reducerSetupScope = EmaxStoreSetupScope<S>()

    private val store by lazy {

        EmaxStore(initialDataState, scope) {
            addMiddleware(LoggerEmaxMiddleware())
            addMiddleware(InitializerEmaxMiddleware { initializer ->
                onInitialized(initializer)
            })
            addMiddleware(
                ViewEventEmaxMiddleware { action ->
                    when (action) {
                        ViewEmaAction.Resumed -> onViewResumed(action)
                        ViewEmaAction.Paused -> onViewPaused(action)
                        ViewEmaAction.Started -> onViewStarted(action)
                        ViewEmaAction.Stopped -> onViewStopped(action)
                    }
                })
            addMiddleware(
                ViewModelEmaxMiddleware<S, A, D>(
                    resultHandler = emaResultHandler,
                    viewModelId = id,
                    navigationState = navigationState,
                    observableSingleEvent = observableSingleEvent
                ) {
                    onSideEffect(it)
                }
            )
            addReducer(
                FeatureEmaxReducer<S, A>(
                    initialState = initialState,
                    reducerAction = { state, action ->
                        onReduce(state, action)
                    }) {
                    currentState = it
                }
            )
            setup()
        }
    }

    protected open fun MiddlewareScope<S>.onViewStarted(action: EmaAction): EmaAction = action
    protected open fun MiddlewareScope<S>.onViewResumed(action: EmaAction): EmaAction = action
    protected open fun MiddlewareScope<S>.onViewPaused(action: EmaAction): EmaAction = action
    protected open fun MiddlewareScope<S>.onViewStopped(action: EmaAction): EmaAction = action

    private val observableState: Flow<EmaState<S>> by lazy {
        store.observableState.map {
            EmaState.Normal(it)
        }
    }


    /**
     * Determine if viewmodel is first time resumed
     *
     */
    private var firstTimeResumed: Boolean = true

    final override fun onAction(action: A) {
        store.dispatch(action)
    }

    protected open fun EmaxStoreSetupScope<S>.setup() = Unit

    context (EmaFeatureReducerScope<S>)
    protected fun S.loading(): S {
        return overlapped(EmaExtraData())
    }

    protected abstract fun EmaFeatureReducerScope<S>.onReduce(state: S, action: A): S

    /**
     * Methods called the first time ViewModel is created
     * @param initializer
     * @param startedFinishListener: (() -> Unit) listener when starting has been finished
     */
    final override fun onCreated(initializer: EmaInitializer?) {
        if (!store.state.checkIsValidStateDataClass()) {
            throw java.lang.IllegalStateException("The EmaDataState class must be a data class")
        }
        store.dispatch(initializer ?: EmaInitializerEmpty)
    }

    /**
     * Call on first time view model is initialized
     */
    protected open fun MiddlewareScope<S>.onInitialized(
        initializer: EmaInitializer
    ): EmaAction = initializer

    protected open fun EmaxViewModelScope<S, D>.onSideEffect(
        action: A
    ): A = action

    final override fun onStartView() {
        store.dispatch(ViewEmaAction.Started)
    }
    /**
     * Called when view is shown in foreground
     */
    final override fun onResumeView() {
        firstTimeResumed = false
        store.dispatch(ViewEmaAction.Resumed)
    }

    /**
     * Called when view is hidden in background
     */
    final override fun onPauseView() {
        store.dispatch(ViewEmaAction.Paused)
    }

    final override fun onStopView() {
        store.dispatch(ViewEmaAction.Stopped)
    }

    /**
     * Get observable state as LiveDaya to avoid state setting from the view
     */
    final override fun subscribeStateUpdates(): Flow<EmaState<S>> = observableState


    final override fun subscribeToActions(): Flow<A> = observableAction.map { it as A }


    /**
     * Get navigation state as LiveData to avoid state setting from the view
     */
    final override fun subscribeToNavigationEvents(): Flow<EmaNavigationDirectionEvent> =
        navigationState

    /**
     * Get single state as LiveData to avoid state setting from the view
     */
    final override fun subscribeToSingleEvents(): Flow<EmaEvent> = observableSingleEvent

    final override fun consumeSingleEvent() {
        observableSingleEvent.tryEmit(EmaEvent.Consumed)
    }

    final override fun consumeNavigation() {
        navigationState.tryEmit(EmaNavigationDirectionEvent.OnNavigated)
    }

    protected fun ResultEventEmaMiddleware(
        id: ResultId,
        onResultAction: MiddlewareScope<S>.(resultAction: ResultEmaAction) -> EmaAction
    ): ResultEventEmaxMiddleware<S> {
        return ResultEventEmaxMiddleware(
            store = store,
            resultId = id,
            ownerId = this.id,
            onResultAction = onResultAction
        )
    }

    final override val onBackHardwarePressedListener: (() -> Boolean)? = null

    /**
     * Method called when the ViewModel is destroyed. It cancels all background pending tasks.
     * Check call name for EmaAndroidView. It uses reflection to call this internal method
     */
    fun onCleared() {
        emaResultHandler.notifyResults(id)
        emaResultHandler.removeResultListener(id)
        scope.cancel()
    }

}