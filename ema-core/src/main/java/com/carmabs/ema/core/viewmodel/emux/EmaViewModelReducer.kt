package com.carmabs.ema.core.viewmodel.emux

import com.carmabs.ema.core.action.FeatureEmaAction
import com.carmabs.ema.core.action.EmaActionDispatcher
import com.carmabs.ema.core.action.ResultEmaAction
import com.carmabs.ema.core.action.ViewEmaAction
import com.carmabs.ema.core.concurrency.EmaMainScope
import com.carmabs.ema.core.constants.INT_ONE
import com.carmabs.ema.core.extension.ResultId
import com.carmabs.ema.core.extension.resultId
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.model.EmaEvent
import com.carmabs.ema.core.navigator.EmaNavigationDirectionEvent
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.core.viewmodel.EmaResultHandler
import com.carmabs.ema.core.viewmodel.EmaViewModel
import com.carmabs.ema.core.viewmodel.emux.middleware.EmaMiddlewareScope
import com.carmabs.ema.core.viewmodel.emux.middleware.EmaNext
import com.carmabs.ema.core.viewmodel.emux.middleware.InitializerEmaMiddleware
import com.carmabs.ema.core.viewmodel.emux.middleware.ResultEventEmaMiddleware
import com.carmabs.ema.core.viewmodel.emux.middleware.ViewModelEmaMiddleware
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

    private val store = EmaStore(initialDataState, scope) {
        addMiddleware(InitializerEmaMiddleware { initializer ->
            onInitializer(initializer)
        })
        addMiddleware(
            ViewModelEmaMiddleware<S,D,A>(
                resultHandler = emaResultHandler,
                viewModelId = id,
                navigationState = navigationState,
                observableSingleEvent = observableSingleEvent
            ) { action ->
                onSideEffect(action)
            })
        addReducer(
            FeatureEmaReducer<S, A>(
                initialState = initialState,
                reducerAction = { state, action ->
                    state.reduceAction(action)
                }) {
                currentState = it
            }
        )
        setup()
    }


    private val emaResultHandler: EmaResultHandler = EmaResultHandler.getInstance()

    /**
     * Observable state that launch event every time a value is set. This value will be a [EmaExtraData]
     * object that can contain any type of object. It will be used for
     * events that only has to be notified once to its observers, e.g: A toast message.
     */
    private val observableSingleEvent: MutableSharedFlow<EmaEvent> = MutableSharedFlow(
        replay = INT_ONE,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

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

    private val observableState: Flow<EmaState<S>> = store.observableState.map {
        EmaState.Normal(it)
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


    private val reducerSetupScope = EmaStoreSetupScope<S>()

    final override fun onAction(action: A) {
        store.dispatch(action)
    }

    protected open fun EmaStoreSetupScope<S>.setup() = Unit

    context (EmaFeatureReducerScope<S>)
    abstract fun S.reduceAction(action: A): S

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
            initializer?.also {
                store.dispatch(initializer)
            }
            startedFinishListener?.invoke()
        } else {
            //We call this to update the data if it has been not be emitted
            // if last time was updated by updateDataState
            startedFinishListener?.invoke()
        }
    }

    /**
     * Call on first time view model is initialized
     */
    abstract fun EmaMiddlewareScope.onInitializer(
        initializer: EmaInitializer
    ): EmaNext

    context (EmaMiddlewareScope)
    abstract fun EmaViewModelScope<D>.onSideEffect(
        action: A
    ): EmaNext

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

    protected fun <S : EmaDataState, A : FeatureEmaAction, D : EmaNavigationEvent> EmaViewModelReducer<S, A, D>.ResultEventEmaMiddleware(
        id: ResultId,
        onResultAction: EmaMiddlewareScope.(resultAction: ResultEmaAction) -> EmaNext
    ): ResultEventEmaMiddleware<S, D> {
        return ResultEventEmaMiddleware(
            store = store,
            resultId = id,
            ownerId = this.id,
            onResultAction = onResultAction
        )
    }

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

sealed interface EmaSampleAction : FeatureEmaAction {
    data class Sample(val a: String) : EmaSampleAction
}

sealed interface EmaNavigatorAction : EmaNavigationEvent {

    data class Nav(val a: String) : EmaNavigatorAction
}

data class SampleDataState(
    val s: String = ""
) : EmaDataState {
    companion object {
        val DEFAULT = SampleDataState("")
    }
}

class S :
    EmaViewModelReducer<SampleDataState, EmaSampleAction, EmaNavigatorAction>(
        SampleDataState.DEFAULT
    ) {

    override fun EmaStoreSetupScope<SampleDataState>.setup() {
        addMiddleware(ResultEventEmaMiddleware(EmaViewModelReducer::class.resultId()){
            next(it)
        })
    }

    override fun EmaMiddlewareScope.onInitializer(initializer: EmaInitializer): EmaNext {
        TODO("Not yet implemented")
    }

    context(EmaFeatureReducerScope<SampleDataState>)
    override fun SampleDataState.reduceAction(
        action: EmaSampleAction
    ): SampleDataState {
        return this
    }



    context(EmaMiddlewareScope)
    override fun EmaViewModelScope<EmaNavigatorAction>.onSideEffect(
        action: EmaSampleAction
    ): EmaNext {
        TODO("Not yet implemented")
    }

    override val onBackHardwarePressedListener: (() -> Boolean)?
        get() = super.onBackHardwarePressedListener
}

