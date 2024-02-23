package com.carmabs.ema.compose.extension

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.carmabs.ema.android.extension.findComponentActivity
import com.carmabs.ema.android.savestate.SaveStateManager
import com.carmabs.ema.compose.action.EmaImmutableActionDispatcher
import com.carmabs.ema.compose.action.toImmutable
import com.carmabs.ema.compose.navigation.EmaComposableTransitions
import com.carmabs.ema.compose.provider.EmaScreenProvider
import com.carmabs.ema.compose.ui.EmaComposableScreen
import com.carmabs.ema.compose.ui.EmaComposableScreenContent
import com.carmabs.ema.compose.ui.handleSaveStateSupport
import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.constants.INT_ZERO
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.model.EmaBackHandlerStrategy
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaViewModel
import kotlin.collections.set


private var mapInitializer: HashMap<String, EmaInitializer>? = null

fun NavController.navigate(
    route: String,
    initializer: EmaInitializer? = null,
    navOptionsBuilder: (NavOptionsBuilder.() -> Unit)
) {
    initializer?.also {
        putInitializer(route, initializer)
    }
    navigate(route, navOptionsBuilder)
}

fun NavController.navigate(
    route: String,
    initializer: EmaInitializer? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    initializer?.also {
        putInitializer(route, initializer)
    }
    navigate(route, navOptions, navigatorExtras)
}

fun <S : EmaDataState, N : EmaNavigationEvent, A : EmaAction.Screen> NavGraphBuilder.createComposableScreen(
    screenContent: EmaComposableScreenContent<S, A>,
    viewModel: () -> EmaViewModel<S, N>,
    onNavigationEvent: (N) -> Unit,
    onBackEvent: ((Any?, EmaImmutableActionDispatcher<A>) -> EmaBackHandlerStrategy)? = null,
    routeId: String = screenContent::class.routeId,
    overrideInitializer: EmaInitializer? = null,
    saveStateManager: SaveStateManager<A, S, N>? = null,
    onViewModelInstance: (@Composable (EmaViewModel<S, N>) -> Unit)? = null,
    fullScreenDialogMode: Boolean = false,
    transitionAnimation: EmaComposableTransitions = EmaComposableTransitions(),
    decoration: @Composable ((content: @Composable () -> Unit, dispatcher: EmaImmutableActionDispatcher<A>) -> Unit)? = null,
    previewRenderState: S? = null
) {
    val content: @Composable (NavGraphBuilder.(NavBackStackEntry) -> Unit) =
        @Composable { _ ->
            val androidVm = EmaScreenProvider.provideComposableViewModel(viewModel = remember {
                viewModel.invoke()
            })

            val vm = androidVm.emaViewModel

            val vmActions = vm.asActionDispatcher<A>().toImmutable()

            val initializerWithSaveStateSupport =
                handleSaveStateSupport(
                    initializer = overrideInitializer ?: getAndReleaseInitializer(routeId),
                    androidViewModel = androidVm,
                    saveStateManager = saveStateManager
                )

            onViewModelInstance?.invoke(vm)
            val screenToDraw = @Composable {
                EmaComposableScreen(
                    initializer = initializerWithSaveStateSupport,
                    onNavigationEvent = onNavigationEvent,
                    onBackEvent = onBackEvent,
                    vm = viewModel,
                    actions = vmActions,
                    screenContent = screenContent,
                    saveStateManager = saveStateManager,
                    previewRenderState = previewRenderState
                )
            }
            decoration?.also {
                it.invoke(screenToDraw, vmActions)
            } ?: also {
                screenToDraw()
            }
        }
    if (fullScreenDialogMode) {
        dialog(route = routeId, dialogProperties = DialogProperties()) {
            content(it)
        }
    } else {
        composable(
            routeId,
            enterTransition = transitionAnimation.enterTransition,
            exitTransition = transitionAnimation.exitTransition,
            popEnterTransition = transitionAnimation.popEnterTransition,
            popExitTransition = transitionAnimation.popExitTransition
        ) {
            content(it)
        }
    }
}

fun NavController.navigateBack(closeActivityWhenBackstackIsEmpty: Boolean = true): Boolean {
    val hasMoreBackScreens = popBackStack()
    if (!hasMoreBackScreens && closeActivityWhenBackstackIsEmpty)
        this.context.findComponentActivity().finish()

    return hasMoreBackScreens
}

fun NavController.navigateToExternalLink(url: String): Boolean {
    return kotlin.runCatching {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        context.findComponentActivity().startActivity(intent)
    }.map { true }.getOrElse { false }
}

fun NavController.getInitializer(route: String? = null): EmaInitializer? {
    return currentDestination?.route?.let {
        getAndReleaseInitializer(it)
    }
}

private fun putInitializer(route: String, initializer: EmaInitializer) {
    val map = mapInitializer ?: hashMapOf<String, EmaInitializer>().apply {
        mapInitializer = this
    }
    map[route] = initializer
}

private fun getAndReleaseInitializer(route: String): EmaInitializer? {
    val initializer = mapInitializer?.remove(route)
    if (mapInitializer?.size == INT_ZERO) {
        mapInitializer = null
    }
    return initializer
}
