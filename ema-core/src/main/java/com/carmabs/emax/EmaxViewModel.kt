package com.carmabs.emax

import com.carmabs.ema.core.action.EmaAction.Lifecycle
import com.carmabs.ema.core.action.EmaAction.Screen
import com.carmabs.ema.core.action.EmaActionDispatcher
import com.carmabs.ema.core.concurrency.EmaMainScope
import com.carmabs.ema.core.constants.INT_ONE
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.initializer.EmptyEmaInitializer
import com.carmabs.ema.core.model.EmaEvent
import com.carmabs.ema.core.navigator.EmaNavigationDirectionEvent
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.core.state.EmaStateDsl
import com.carmabs.ema.core.viewmodel.EmaResultHandler
import com.carmabs.ema.core.viewmodel.EmaViewModel
import com.carmabs.emax.middleware.log.LoggerEmaxMiddleware
import com.carmabs.emax.middleware.viewmodel.SideEffectBuilder
import com.carmabs.emax.middleware.viewmodel.ViewModelEmaxMiddleware
import com.carmabs.emax.reducer.EmaxReducerActionFilter
import com.carmabs.emax.store.EmaxStore
import com.carmabs.emax.store.StoreSetupScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * View model to handle view states.
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */

@EmaStateDsl
abstract class EmaxViewModel<S : EmaDataState, A : Screen, N : EmaNavigationEvent>(
    initialDataState: S,
    defaultScope: CoroutineScope = EmaMainScope()
) : EmaViewModel<S, N>, EmaActionDispatcher<A> {

    /**
     * The scope where coroutines will be launched by default.
     */
    private var scope: CoroutineScope = defaultScope

    final override fun setScope(scope: CoroutineScope) {
        this.scope = scope
    }

    final override val initialState: EmaState<S> = EmaState.Normal(initialDataState)

    private val emaResultHandler: EmaResultHandler = EmaResultHandler.getInstance()


    /**
     * To determine if the view must be updated when view model is created automatically
     */
    protected open val updateOnInitialization: Boolean = true

    /**
     * Used to know if state has been updated at least once
     */
    private var hasBeenUpdated = false
    override val shouldRenderState: Boolean
        get() = updateOnInitialization || hasBeenUpdated

    /**
     * Observable state that launch event every time a value is set. [N] value be will a [EmaNavigationEvent]
     * object that represent the destination. This observable will be used for
     * events that only has to be notified once to its observers and is used to notify the navigation
     * events
     */
    private val navigationState: MutableSharedFlow<EmaNavigationDirectionEvent> = MutableSharedFlow(
        replay = INT_ONE,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val channelAction = Channel<Screen>()

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

    private val reducerSetupScope = StoreSetupScope<S>()

    private val viewModelMiddleware: ViewModelEmaxMiddleware<EmaState<S>, A, N> by lazy {
        ViewModelEmaxMiddleware(
            navigationState = navigationState,
            observableSingleEvent = observableSingleEvent
        ) {
            onSideEffectSetup()
        }
    }


    private val stateReducer = EmaxReducerActionFilter<EmaState<S>, Screen>(filterClass = Screen::class) {
        val state = this
        this@EmaxViewModel.run {
            hasBeenUpdated = true
            state.onReduce(it as A)
        }
    }

    private val store by lazy {
        EmaxStore(initialState, scope) {
            addMiddleware(LoggerEmaxMiddleware())
            addMiddleware(viewModelMiddleware)
            addReducer(stateReducer)
            setup()
        }
    }

    private val observableState: Flow<EmaState<S>> by lazy {
        store.observableState.filter {
            shouldRenderState
        }
    }


    /**
     * Determine if viewmodel is first time resumed
     *
     */
    private var firstTimeResumed: Boolean = true


    final override fun dispatch(action: A) {
        store.dispatch(action)
    }

    protected open fun StoreSetupScope<EmaState<S>>.setup() = Unit
    protected open fun EmaState<S>.onReduceInitialization(
        initializer: EmaInitializer
    ): EmaState<S> = this

    protected abstract fun EmaState<S>.onReduce(action: A):EmaState<S>

    /**
     * Methods called the first time ViewModel is created
     * @param initializer
     */
    final override fun onCreated(initializer: EmaInitializer?) {
        if (!store.state.checkIsValidStateDataClass()) {
            throw java.lang.IllegalStateException("The EmaDataState class must be a data class")
        }
        store.dispatch(initializer ?: EmptyEmaInitializer)
    }

    protected open fun SideEffectBuilder<EmaState<S>, A, N>.onSideEffectSetup() =
        Unit


    final override fun onStartView() {
        store.dispatch(Lifecycle.Started)
    }

    /**
     * Called when view is shown in foreground
     */
    final override fun onResumeView() {
        firstTimeResumed = false
        store.dispatch(Lifecycle.Resumed)
    }

    /**
     * Called when view is hidden in background
     */
    final override fun onPauseView() {
        store.dispatch(Lifecycle.Paused)
    }

    final override fun onStopView() {
        store.dispatch(Lifecycle.Stopped)
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

    final override fun notifyOnNavigated() {
        navigationState.tryEmit(EmaNavigationDirectionEvent.OnNavigated)
    }

    override fun onActionBackHardwarePressed() {
        viewModelMiddleware.onActionBackHardwarePressed()
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