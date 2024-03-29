package com.carmabs.ema.core.view

import com.carmabs.ema.core.initializer.EmaInitializerSerializer
import com.carmabs.ema.core.model.EmaEvent
import com.carmabs.ema.core.model.onLaunched
import com.carmabs.ema.core.navigator.EmaNavigationDirection
import com.carmabs.ema.core.navigator.EmaNavigationDirectionEvent
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.core.navigator.onNavigation
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.core.state.EmaStateTransition
import com.carmabs.ema.core.viewmodel.EmaViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.jvm.internal.PropertyReference0
import kotlin.reflect.KProperty


/**
 * View to handle VM view logic states through [EmaState].
 * The user must provide in the constructor by template:
 *  - The view model class [EmaViewModel] is going to use the view
 *  - The navigation state class [EmaNavigationEvent] will handle the navigation
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
interface EmaView<S : EmaDataState, VM : EmaViewModel<S, N>, N : EmaNavigationEvent> {

    /**
     * Scope for flow updates
     */
    val coroutineScope: CoroutineScope

    /**
     * The view model [EmaViewModel] for the view
     */
    val viewModel: VM

    /**
     * The navigator [EmaNavigator]
     */
    val navigator: EmaNavigator<N>?

    /**
     * The initializer from previous views when it is launched.
     */
    val initializerSerializer:EmaInitializerSerializer?

    /**
     * The previous state of the View
     */
    var previousEmaState: EmaState<S, N>?

    val previousStateData: S?
        get() = previousEmaState?.data


    /**
     * Trigger to start viewmodel only when startViewModel is launched
     */
    val startTrigger: EmaViewModelTrigger?

    /**
     * Called when view model trigger an update view event
     * @param state of the view
     */
    private fun onDataUpdated(state: EmaState<S, N>) {

        previousEmaState?.let { previousState ->
            if (previousState.javaClass.name != state.javaClass.name) {
                when (state) {
                    is EmaState.Overlapped -> {
                        onEmaStateTransition(
                            EmaStateTransition.NormalToOverlapped(
                                previousState.data,
                                state.extraData
                            )
                        )
                    }

                    is EmaState.Normal -> {
                        onEmaStateTransition(
                            EmaStateTransition.OverlappedToNormal(
                                (previousState as EmaState.Overlapped<S, N>).extraData,
                                state.data
                            )
                        )
                    }
                }
            }
        }

        onEmaStateNormal(state.data)
        when (state) {
            is EmaState.Overlapped -> {
                onEmaStateOverlapped(state.extraData)
            }

            else -> {
                //DO NOTHING
            }
        }

        previousEmaState = state
    }

    fun onEmaStateTransition(transition: EmaStateTransition) = Unit

    /**
     * Check EMA state selected property to execute action with new value if it has changed
     * @param action Action to execute. Current value passed in lambda.
     * @param field Ema State field to check if it has been changed.
     * @param areEqualComparator Comparator to determine if both objects are equals. Useful for complex objects
     * @return true if it has been updated, false otherwise
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> bindForUpdate(
        field: KProperty<T>,
        areEqualComparator: ((old: T, new: T) -> Boolean)? = null,
        action: (new: T) -> Unit
    ): Boolean {
        var updated = false
        val currentClass = (field as PropertyReference0).boundReceiver as? S
        currentClass?.also { _ ->
            val currentValue = (field.get() as T)
            previousEmaState?.data?.also {
                try {
                    val previousField = it.javaClass.getDeclaredField(field.name)
                    previousField.isAccessible = true
                    val previousValue = previousField.get(previousEmaState?.data) as T
                    if (areEqualComparator?.invoke(previousValue, currentValue)?.not()
                            ?: (previousValue != currentValue)
                    ) {
                        updated = true
                        action.invoke(currentValue)
                    }
                } catch (e: Exception) {
                    println("EMA : Field not found")
                }
            } ?: action.invoke(currentValue)
        } ?: println("EMA : Bounding class must be the state of the view")
        return updated
    }

    /**
     * Check EMA state selected property to execute action with new value if it has changed
     * @param action Action to execute. Current and previous value passed in lambda
     * @param field Ema State field to check if it has been changed
     * @param areEqualComparator Comparator to determine if both objects are equals. Useful for complex objects
     * @return true if it has been updated, false otherwise
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> bindForUpdateWithPrevious(
        field: KProperty<T>,
        areEqualComparator: ((old: T, new: T) -> Boolean)? = null,
        action: (old: T?, new: T) -> Unit
    ): Boolean {
        var updated = false
        val currentClass = (field as PropertyReference0).boundReceiver as? S
        currentClass?.also { _ ->
            val currentValue = (field.get() as T)
            previousEmaState?.data?.also {
                try {
                    val previousField = it.javaClass.getDeclaredField(field.name)
                    previousField.isAccessible = true
                    val previousValue = previousField.get(previousEmaState?.data) as T
                    if (areEqualComparator?.invoke(previousValue, currentValue)?.not()
                            ?: (previousValue != currentValue)
                    ) {
                        updated = true
                        action.invoke(previousValue, currentValue)
                    }
                } catch (e: Exception) {
                    println("EMA : Field not found")
                }
            } ?: action.invoke(null, currentValue)
        } ?: println("EMA : Bounding class must be the state of the view")
        return updated
    }

    /**
     * Called when view model trigger an only once notified event
     * @param event for extra information
     */
    fun onSingleData(event: EmaEvent) {
        event.onLaunched {
            onSingleEvent(it)
            viewModel.consumeSingleEvent()
        }

    }

    /**
     * Called when view model trigger a navigation event for navigation
     * @param navigation state with information about the destination
     */
    fun onNavigation(navigation: EmaNavigationDirectionEvent) {
        navigation.onNavigation {
            when (val direction = it) {
                is EmaNavigationDirection.Back -> {
                    navigateBack(direction.result)
                }

                is EmaNavigationDirection.Forward -> {
                    navigate(direction.navigationEvent as N)
                }
            }
            viewModel.notifyOnNavigated()
        }
    }

    /**
     * Called when view model trigger an update view event
     * @param data with the state of the view
     */
    fun onEmaStateNormal(data: S)

    /**
     * Called when view model trigger a updateOverlappedState event
     * @param extra with information about updateOverlappedState
     */
    fun onEmaStateOverlapped(extra: EmaExtraData)

    /**
     * Called when view model trigger an only once notified event.Not called when the view is first time attached to the view model
     * @param extra with information about updateAlternativeState
     */
    fun onSingleEvent(extra: EmaExtraData)

    /**
     * Called when view model trigger a navigation event
     * @param navigationEvent for the navigation event data
     */

    fun navigate(navigationEvent: N) {
        navigator?.navigate(navigationEvent) ?: throwNavigationException()
    }

    @Throws
    private fun throwNavigationException() {
        throw RuntimeException("You must provide an EmaNavigator as navigator to handle the navigation")
    }

    /**
     * Called when view model trigger a navigation back event
     * @return True
     */
    fun navigateBack(result: Any? = null): Boolean {
        return navigator?.navigateBack(result) ?: onBack(result)
    }

    fun onBack(result: Any?): Boolean

    fun onCreate(viewModel: VM) {
        startTrigger?.also {
            it.triggerAction = {
                viewModel.onCreated(initializerSerializer?.restore())
            }
        } ?: also {
            viewModel.onCreated(initializerSerializer?.restore())
        }
    }

    /**
     * Called when view model is started
     */
    fun onStartView(viewModel: VM) {
        startTrigger?.also {
            it.triggerAction = {
                viewModel.onStartView()
            }
        } ?: also {
            viewModel.onStartView()
        }
    }


    /**
     * Called when view state is bound to viewmodel
     */
    fun onBindView(coroutineScope: CoroutineScope, viewModel: VM): MutableList<Job> {
        val jobList = mutableListOf<Job>()
        jobList.add(onBindState(coroutineScope, viewModel))
        jobList.add(onBindSingle(coroutineScope, viewModel))
        jobList.add(onBindNavigation(coroutineScope, viewModel))
        return jobList
    }

    /**
     * Called when view state is bound to viewmodel
     */

    fun onBindState(coroutineScope: CoroutineScope, viewModel: VM): Job {
        return coroutineScope.launch {
            viewModel.subscribeStateUpdates().collectLatest {
                onDataUpdated(it)
            }
        }
    }

    fun onBindNavigation(coroutineScope: CoroutineScope, viewModel: VM): Job {
        return coroutineScope.launch {
            viewModel.subscribeToNavigationEvents().collectLatest {
                onNavigation(it)
            }
        }
    }

    fun onBindSingle(coroutineScope: CoroutineScope, viewModel: VM): Job {
        return coroutineScope.launch {
            viewModel.subscribeToSingleEvents().collect {
                onSingleData(it)
            }
        }
    }

    /**
     * Used to notify the view model that view has been gone to foreground.
     */
    fun onResumeView(viewModel: VM) {
        startTrigger?.also {
            if (it.hasBeenStarted)
                viewModel.onResumeView()
        } ?: also {
            viewModel.onResumeView()
        }
    }


    fun onPauseView(viewModel: VM) {
        viewModel.onPauseView()
    }


    fun onUnbindView(viewJob: MutableList<Job>?, viewModel: VM) {
        viewJob?.forEach {
            try {
                if (!it.isCancelled && !it.isCompleted)
                    it.cancel()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        viewJob?.clear()
        viewModel.onStopView()
    }
}