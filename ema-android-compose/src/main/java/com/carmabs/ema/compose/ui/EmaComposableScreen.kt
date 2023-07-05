package com.carmabs.ema.compose.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.action.EmaActionDispatcher
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.core.viewmodel.EmaViewModel
import kotlinx.coroutines.flow.collect
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.javaMethod

@Composable
fun <S : EmaDataState, VM : EmaViewModel<S, D>, D : EmaDestination, A : EmaAction> EmaComposableScreen(
    initializer: EmaInitializer? = null,
    vm: VM,
    actions: EmaActionDispatcher<A>,
    navigator: EmaNavigator<D>,
    screenContent: EmaComposableScreenContent<S, A>
) {
    vm.onBackHardwarePressedListener?.also {
        BackHandler(true) {
            if (it.invoke())
                navigator.navigateBack()
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
        vm.getNavigationState()
            .flowWithLifecycle(lifecycle)
            .collect { destination ->
                destination?.also { dest ->
                    if (!dest.isNavigated)
                        navigator.navigate(dest)
                    Class.forName(dest.javaClass.name).kotlin.functions.find { it.name == "setNavigated" }?.javaMethod?.invoke(
                        dest
                    )
                } ?: navigator.navigateBack()
            }
    }

    screenContent.onStateNormal(state.data, actions)
    drawOverlappedState(screenContent, actions, (state as? EmaState.Overlapped)?.dataOverlapped)

    val event = vm.getSingleObservableState()
        .collectAsStateWithLifecycle(initialValue = EmaExtraData(), lifecycle = lifecycle)
    val context = LocalContext.current
    LaunchedEffect(key1 = event.value) {
        screenContent.onSingleEvent(context, event.value, actions)
    }
}

//We call this with a composable function to avoid changing the tree composer and this way avoid
//new composition of internal onNormal/onOverlapped composable states
@Composable
private fun <S : EmaDataState, A : EmaAction> drawOverlappedState(
    screenContent: EmaComposableScreenContent<S, A>,
    actions: EmaActionDispatcher<A>,
    extraData: EmaExtraData?
) {
    extraData?.also {
        screenContent.onStateOverlapped(extra = it, actions = actions)
    }
}
