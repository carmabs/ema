package com.carmabs.ema.compose.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.action.EmaActionDispatcher
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.core.viewmodel.EmaViewModel
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.javaMethod

@Composable
fun <S : EmaDataState, VM : EmaViewModel<S, D>, D : EmaDestination, A : EmaAction> EmaComposableScreen(
    initializer: EmaInitializer? = null,
    vm: VM,
    actions: EmaActionDispatcher<A>,
    screenContent: EmaComposableScreenContent<S, A>,
    onNavigationEvent: (D) -> Boolean,
    onNavigationBackEvent: () -> Boolean
) {
    vm.onBackHardwarePressedListener?.also {
        BackHandler(true) {
            if (it.invoke())
                onNavigationBackEvent.invoke()
        }
    }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            val TAG = "LIFECYCLE"
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    Log.d(TAG, "On create")
                }

                Lifecycle.Event.ON_START -> {
                    Log.d(TAG, "On start")
                    vm.onStart(initializer)
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
            lifecycle.removeObserver(observer)
        }
    }

    val state = vm.getObservableState()
        .collectAsStateWithLifecycle(initialValue = vm.initialState, lifecycle = lifecycle).value


    LaunchedEffect(key1 = Unit) {
        vm.getNavigationState().collect { destination ->
            destination?.also { dest ->
                if (!dest.isNavigated) {
                    val navigated = onNavigationEvent(dest)
                    if (navigated) {
                        Class.forName(dest.javaClass.name).kotlin.functions.find { it.name == "setNavigated" }?.javaMethod?.invoke(
                            dest
                        )
                    }
                }
            } ?: onNavigationBackEvent.invoke()
        }
    }

    screenContent.onStateNormal(state.data, actions)
    OverlappedComposable((state as? EmaState.Overlapped), screenContent, actions)


    val event = vm.getSingleObservableState().collectAsStateWithLifecycle(initialValue = EmaExtraData(), lifecycle = lifecycle).value
    val context = LocalContext.current
    LaunchedEffect(key1 = event) {
        screenContent.onSingleEvent(context, event, actions)
    }
}

@Composable
private fun <S : EmaDataState, A : EmaAction> OverlappedComposable(
    overlappedState: EmaState.Overlapped<S>? = null,
    screenContent: EmaComposableScreenContent<S, A>,
    actions: EmaActionDispatcher<A>
) {
    overlappedState?.also {
        screenContent.onStateOverlapped(extra = overlappedState.dataOverlapped, actions = actions)
    }
}