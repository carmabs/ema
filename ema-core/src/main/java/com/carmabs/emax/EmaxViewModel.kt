package com.carmabs.emax

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.action.EmaAction.Lifecycle
import com.carmabs.ema.core.action.EmaAction.Screen
import com.carmabs.ema.core.action.EmaActionDispatcher
import com.carmabs.ema.core.concurrency.EmaMainScope
import com.carmabs.ema.core.extension.distinctNavigationChanges
import com.carmabs.ema.core.extension.distinctSingleEventChanges
import com.carmabs.ema.core.extension.distinctStateDataChanges
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.model.EmaEvent
import com.carmabs.ema.core.navigator.EmaNavigationDirectionEvent
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.core.state.EmaStateDsl
import com.carmabs.ema.core.viewmodel.EmaResultHandler
import com.carmabs.ema.core.viewmodel.EmaViewModel
import com.carmabs.emax.middleware.common.emaxMiddlewareOf
import com.carmabs.emax.middleware.log.LoggerEmaxMiddleware
import com.carmabs.emax.reducer.EmaxReducer
import com.carmabs.emax.reducer.OnBackReducer
import com.carmabs.emax.store.EmaxStore
import com.carmabs.emax.store.StoreSetupScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
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
    stateReducer: EmaxReducer<EmaState<S, N>>,
    defaultScope: CoroutineScope = EmaMainScope()
) : EmaViewModel<S, N>, EmaActionDispatcher<A> {

    /**
     * The scope where coroutines will be launched by default.
     */
    private var scope: CoroutineScope = defaultScope

    final override fun setScope(scope: CoroutineScope) {
        this.scope = scope
    }

    final override val initialState: EmaState<S, N> = EmaState.Normal(initialDataState)

    private val emaResultHandler: EmaResultHandler = EmaResultHandler.getInstance()


    /**
     * To determine if the view must be updated when view model is created automatically
     */
    protected open val updateOnInitialization: Boolean = true

    /**
     * Used to know if state has been updated at least once
     */
    private var hasBeenUpdated = false
    final override val shouldRenderState: Boolean
        get() = updateOnInitialization || hasBeenUpdated

    private val channelAction = Channel<Screen>()

    private val observableAction = channelAction.receiveAsFlow()

    private val storeSetupScope = StoreSetupScope<S>()

    private val store by lazy {
        EmaxStore(initialState, scope) {
            addMiddleware(LoggerEmaxMiddleware())
            addMiddleware(emaxMiddlewareOf(actionFilter = EmaInitializer::class) {
                if(it !is EmaInitializer.EMPTY)
                    hasBeenUpdated = true
            })
            addMiddleware(emaxMiddlewareOf(actionFilter = Screen::class) {
                hasBeenUpdated = true
            })
            addReducer(stateReducer)
            addReducer(OnBackReducer())
            setup()
        }
    }

    private val observableState: Flow<EmaState<S, N>> by lazy {
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

    protected open fun StoreSetupScope<EmaState<S, N>>.setup() = Unit

    /**
     * Methods called the first time ViewModel is created
     * @param initializer
     */
    final override fun onCreated(initializer: EmaInitializer?) {
        if (!store.state.checkIsValidStateDataClass()) {
            throw java.lang.IllegalStateException("The EmaDataState class must be a data class")
        }
        store.dispatch(initializer ?: EmaInitializer.EMPTY)
    }

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
    final override fun subscribeStateUpdates(): Flow<EmaState<S, N>> =
        observableState.distinctStateDataChanges()


    final override fun subscribeToActions(): Flow<A> = observableAction.map { it as A }


    /**
     * Get navigation state as LiveData to avoid state setting from the view
     */
    final override fun subscribeToNavigationEvents(): Flow<EmaNavigationDirectionEvent> =
        observableState.distinctNavigationChanges()

    /**
     * Get single state as LiveData to avoid state setting from the view
     */
    final override fun subscribeToSingleEvents(): Flow<EmaEvent> =
        observableState.distinctSingleEventChanges()

    final override fun consumeSingleEvent() {
        store.dispatch(EmaAction.ViewModel.ConsumeSingleEvent)
    }

    final override fun notifyOnNavigated() {
        store.dispatch(EmaAction.ViewModel.OnNavigated)
    }

    final override fun onActionBackHardwarePressed() {
        store.dispatch(EmaAction.ViewModel.NavigationBack)
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