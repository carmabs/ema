package com.carmabs.ema.core.viewmodel.emux

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
import com.carmabs.ema.core.viewmodel.emux.middleware.log.EmaLoggerMiddleware
import com.carmabs.ema.core.viewmodel.emux.middleware.common.MiddlewareScope
import com.carmabs.ema.core.viewmodel.emux.middleware.initializer.InitializerEmaMiddleware
import com.carmabs.ema.core.viewmodel.emux.middleware.result.ResultEventEmaMiddleware
import com.carmabs.ema.core.viewmodel.emux.middleware.viewmodel.ViewModelEmaMiddleware
import com.carmabs.ema.core.viewmodel.emux.reducer.EmaFeatureReducerScope
import com.carmabs.ema.core.viewmodel.emux.reducer.FeatureEmaReducer
import com.carmabs.ema.core.viewmodel.emux.store.EmaStore
import com.carmabs.ema.core.viewmodel.emux.store.EmaStoreSetupScope
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
abstract class EmaViewModelReducer<S : EmaDataState, A : FeatureEmaAction, D : EmaNavigationEvent>(
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

    private val reducerSetupScope = EmaStoreSetupScope<S>()

    private val store by lazy {

        EmaStore(initialDataState, scope) {
            addMiddleware(EmaLoggerMiddleware())
            addMiddleware(InitializerEmaMiddleware { initializer ->
                onInitializer(initializer)
            })
            addMiddleware(
                ViewModelEmaMiddleware<S, D, A>(
                    resultHandler = emaResultHandler,
                    viewModelId = id,
                    navigationState = navigationState,
                    observableSingleEvent = observableSingleEvent
                ) {
                     onSideEffect(it)
                }
            )
            addReducer(
                FeatureEmaReducer<S, A>(
                    initialState = initialState,
                    reducerAction = { state, action ->
                            reduceAction(state,action)
                    }) {
                    currentState = it
                }
            )
            setup()
        }
    }

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

    /**
     * Determine if the viewmodel has initialized its state
     */

    protected var hasBeenInitialized: Boolean = false
        private set


    final override fun onAction(action: A) {
        store.dispatch(action)
    }

    protected abstract fun EmaStoreSetupScope<S>.setup()

    protected abstract fun EmaFeatureReducerScope<S>.reduceAction(state:S,action: A): S

    /**
     * Methods called the first time ViewModel is created
     * @param initializer
     * @param startedFinishListener: (() -> Unit) listener when starting has been finished
     */
    final override fun onStart(initializer: EmaInitializer?, startedFinishListener: (() -> Unit)?) {
        if (!hasBeenInitialized) {
            if (!store.state.checkIsValidStateDataClass()) {
                throw java.lang.IllegalStateException("The EmaDataState class must be a data class")
            }
            hasBeenInitialized = true
            store.dispatch(initializer ?: EmaInitializerEmpty)
        } else {
            //We call this to update the data if it has been not be emitted
            // if last time was updated by updateDataState

        }
        store.dispatch(ViewEmaAction.Started)
        startedFinishListener?.invoke()
    }

    /**
     * Call on first time view model is initialized
     */
    protected abstract fun MiddlewareScope<S>.onInitializer(
        initializer: EmaInitializer
    ): EmaAction

    protected abstract fun EmaViewModelScope<S,D>.onSideEffect(
        action: A
    ): EmaAction

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


    fun subscribeToActions(): Flow<A> = observableAction.map { it as A }


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
    ): ResultEventEmaMiddleware<S> {
        return ResultEventEmaMiddleware(
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