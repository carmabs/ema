package com.carmabs.ema.compose.ui

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carmabs.ema.android.extension.addOnBackPressedListener
import com.carmabs.ema.android.navigation.EmaNavigationBackHandler
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.compose.action.EmaImmutableActionDispatcher
import com.carmabs.ema.compose.action.EmaImmutableActionDispatcherEmpty
import com.carmabs.ema.compose.action.toImmutable
import com.carmabs.ema.compose.extension.activity
import com.carmabs.ema.compose.extension.asActionDispatcher
import com.carmabs.ema.compose.extension.skipForPreview
import com.carmabs.ema.compose.provider.EmaScreenProvider
import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.action.EmaActionDispatcher
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.model.EmaBackHandlerStrategy
import com.carmabs.ema.core.model.EmaEvent
import com.carmabs.ema.core.navigator.EmaNavigationDirection
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.navigator.onNavigation
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.core.viewmodel.EmaViewModel

@Composable
fun <S : EmaDataState, VM : EmaViewModel<S, D>, D : EmaNavigationEvent, A : EmaAction.Screen> EmaComposableScreen(
    initializer: EmaInitializer? = null,
    vm: VM,
    actions: EmaActionDispatcher<A>,
    screenContent: EmaComposableScreenContent<S, A>,
    onNavigationEvent: (D) -> Unit,
    onBackEvent: ((Any?, EmaImmutableActionDispatcher<A>) -> EmaBackHandlerStrategy)? = null,
    actionSubscriber: ((A) -> Unit)? = null,
    previewRenderState: S? = null
) {
    skipForPreview(
        previewComposable = {
            previewRenderState?.also { previewState ->
                screenContent.onStateNormal(
                    state = previewState,
                    actions = EmaImmutableActionDispatcherEmpty()
                )
            }

        }
    ) {
        val immutableActions = remember {
            actions.toImmutable()
        }

        renderScreen(
            initializer,
            screenContent,
            vm,
            immutableActions,
            onNavigationEvent,
            onBackEvent,
            actionSubscriber
        )
    }
}

@Composable
fun <S : EmaDataState, D : EmaNavigationEvent, A : EmaAction.Screen> EmaComposableScreen(
    initializer: EmaInitializer? = null,
    vm: () -> EmaAndroidViewModel<S, D>,
    screenContent: EmaComposableScreenContent<S, A>,
    onNavigationEvent: (D) -> Unit,
    onBackEvent:((Any?, EmaImmutableActionDispatcher<A>) -> EmaBackHandlerStrategy)? = null,
    actionsSubscriber: ((A) -> Unit)? = null,
    previewRenderState: S? = null
) {
    skipForPreview(
        previewComposable = {
            previewRenderState?.also { previewState ->
                screenContent.onStateNormal(
                    state = previewState,
                    actions = EmaImmutableActionDispatcherEmpty()
                )
            }

        }
    ) {
        val viewModel = EmaScreenProvider.provideComposableViewModel(androidViewModel = remember {
            vm.invoke()
        })

        val immutableActions = remember {
            viewModel.asActionDispatcher<A>().toImmutable()
        }

        renderScreen(
            initializer,
            screenContent,
            viewModel,
            immutableActions,
            onNavigationEvent,
            onBackEvent,
            actionsSubscriber
        )
    }
}

@Composable
private fun <A : EmaAction.Screen, D : EmaNavigationEvent, S : EmaDataState> renderScreen(
    initializer: EmaInitializer?,
    screenContent: EmaComposableScreenContent<S, A>,
    vm: EmaViewModel<S, D>,
    immutableActions: EmaImmutableActionDispatcher<A>,
    onNavigationEvent: (D) -> Unit,
    onBackEvent: ((Any?, EmaImmutableActionDispatcher<A>) -> EmaBackHandlerStrategy)? = null,
    actionSubscriber: ((A) -> Unit)? = null,
    ) {
    val currentActivity = LocalContext.activity
    //We want to use onBackEvent to enable manual back navigation from viewmodel to handle
    //back result
    val backHandler = onBackEvent?.let {
        rememberBackHardwareClickHandler(
            currentActivity,
            vm
        )
    }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            val TAG = "LIFECYCLE"
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    Log.d(TAG, "On create")
                    vm.onCreated(initializer)
                }

                Lifecycle.Event.ON_START -> {
                    Log.d(TAG, "On start")
                    vm.onStartView()
                }

                Lifecycle.Event.ON_RESUME -> {
                    Log.d(TAG, "On resume")
                    vm.onResumeView()
                }

                Lifecycle.Event.ON_PAUSE -> {
                    Log.d(TAG, "On pause")
                    vm.onPauseView()
                }

                Lifecycle.Event.ON_STOP -> {
                    Log.d(TAG, "On stop")
                    vm.onStopView()
                }

                Lifecycle.Event.ON_DESTROY -> {
                    Log.d(TAG, "On destroy")
                }

                Lifecycle.Event.ON_ANY -> {
                    Log.d(TAG, "On any")
                }
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            vm.onPauseView()
            vm.onStopView()
            lifecycle.removeObserver(observer)
        }
    }

    val state = vm.subscribeStateUpdates()
        .collectAsStateWithLifecycle(initialValue = vm.initialState, lifecycle = lifecycle).value

    LaunchedEffect(key1 = Unit) {
        vm.subscribeToNavigationEvents().collect { event ->
            event.onNavigation { eventData ->
                when (eventData) {
                    is EmaNavigationDirection.Back -> {
                        when (val strategy =
                            onBackEvent?.invoke(eventData.result, immutableActions)) {
                            EmaBackHandlerStrategy.Cancelled -> {
                                //DO NOTHING
                            }

                            is EmaBackHandlerStrategy.ContinueOnBackPressed -> {
                                backHandler?.value?.remove()
                                currentActivity.onBackPressedDispatcher.onBackPressed()
                                if (!strategy.removeBackHandler) {
                                    backHandler?.value?.restore()
                                }
                            }

                            null -> {
                                currentActivity.onBackPressedDispatcher.onBackPressed()
                            }
                        }
                    }

                    is EmaNavigationDirection.Forward -> {
                        onNavigationEvent(eventData.navigationEvent as D)
                    }
                }
                vm.notifyOnNavigated()
            }
        }
    }

    if(vm.shouldRenderState) {
        screenContent.onStateNormal(state.data, immutableActions)
        OverlappedComposable((state as? EmaState.Overlapped), screenContent, immutableActions)
    }

    val event = vm.subscribeToSingleEvents()
        .collectAsStateWithLifecycle(initialValue = EmaExtraData(), lifecycle = lifecycle).value
    val context = LocalContext.current
    LaunchedEffect(key1 = event) {
        if (event is EmaEvent.Launched) {
            screenContent.onSingleEvent(context, event.data, immutableActions)
            vm.consumeSingleEvent()
        }
    }

    actionSubscriber?.also {
        LaunchedEffect(key1 = Unit) {
            vm.asActionDispatcher<A>().subscribeToActions().collect{
                actionSubscriber(it)
            }
        }
    }
}

@Composable
private fun <D : EmaNavigationEvent, S : EmaDataState> rememberBackHardwareClickHandler(
    activity: ComponentActivity,
    vm: EmaViewModel<S, D>
): MutableState<EmaNavigationBackHandler?> {
    val backHandlerState: MutableState<EmaNavigationBackHandler?> = remember {
        mutableStateOf(null)
    }
    DisposableEffect(key1 = Unit) {
        backHandlerState.value =
            activity.addOnBackPressedListener {
                vm.onActionBackHardwarePressed()
                //Cancel because we are handling manually the navigation with onActionBackHardwarePressed()
                EmaBackHandlerStrategy.Cancelled

            }
        onDispose {
            backHandlerState.value?.remove()
        }
    }
    return backHandlerState
}

@Composable
private fun <S : EmaDataState, A : EmaAction.Screen> OverlappedComposable(
    overlappedState: EmaState.Overlapped<S>? = null,
    screenContent: EmaComposableScreenContent<S, A>,
    actions: EmaImmutableActionDispatcher<A>
) {
    overlappedState?.also {
        screenContent.onStateOverlapped(extra = overlappedState.extraData, actions = actions)
    }
}