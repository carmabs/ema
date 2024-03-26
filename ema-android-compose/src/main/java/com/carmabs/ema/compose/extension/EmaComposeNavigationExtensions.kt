package com.carmabs.ema.compose.extension

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.carmabs.ema.android.extension.findComponentActivity
import com.carmabs.ema.android.extension.getInitializer
import com.carmabs.ema.android.initializer.EmaInitializerBundle
import com.carmabs.ema.android.initializer.bundle.strategy.BundleSerializerStrategy
import com.carmabs.ema.android.initializer.savestate.SaveStateHandler
import com.carmabs.ema.compose.action.EmaImmutableActionDispatcher
import com.carmabs.ema.compose.action.toImmutable
import com.carmabs.ema.compose.initializer.EmaInitializerSupport
import com.carmabs.ema.compose.navigation.EmaComposableTransitions
import com.carmabs.ema.compose.navigation.EmaInitializerNavType
import com.carmabs.ema.compose.provider.EmaScreenProvider
import com.carmabs.ema.compose.ui.EmaComposableScreen
import com.carmabs.ema.compose.ui.EmaComposableScreenContent
import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.model.EmaBackHandlerStrategy
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaViewModel

fun NavController.navigate(
    route: String,
    initializerBundle: EmaInitializerBundle?,
    navOptionsBuilder: (NavOptionsBuilder.() -> Unit)
) {
    val routeParsed = routeWithInitializer(route, initializerBundle)
    navigate(routeParsed, navOptionsBuilder)
}

fun NavController.navigate(
    route: String,
    initializerBundle: EmaInitializerBundle?,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    val routeParsed = routeWithInitializer(route, initializerBundle)
    navigate(routeParsed, navOptions, navigatorExtras)
}

fun <S : EmaDataState, A : EmaAction.Screen, N : EmaNavigationEvent> NavGraphBuilder.createComposableScreen(
    screenContent: EmaComposableScreenContent<S, A>,
    viewModel: () -> EmaViewModel<S, N>,
    onNavigationEvent: (N) -> Unit,
    onBackEvent: ((Any?, EmaImmutableActionDispatcher<A>) -> EmaBackHandlerStrategy)? = null,
    routeId: String = screenContent::class.routeId,
    initializerSupport: EmaInitializerSupport? = null,
    saveStateHandler: SaveStateHandler<S, N>? = null,
    onViewModelInstance: (@Composable (EmaViewModel<S, N>) -> Unit)? = null,
    fullScreenDialogMode: Boolean = false,
    transitionAnimation: EmaComposableTransitions = EmaComposableTransitions(),
    decoration: @Composable ((content: @Composable () -> Unit, dispatcher: EmaImmutableActionDispatcher<A>) -> Unit)? = null,
    previewRenderState: S? = null
) {
    val content: @Composable (NavGraphBuilder.(NavBackStackEntry) -> Unit) =
        @Composable { backEntry ->

            val androidVm = EmaScreenProvider.provideComposableViewModel(viewModel = remember {
                viewModel.invoke()
            })

            val vm = androidVm.emaViewModel

            val vmActions = remember {
                vm.asActionDispatcher<A>().toImmutable()
            }

            val initializer = initializerSupport?.let {
                it.overrideInitializer?: backEntry.arguments?.getInitializer(it.serializerStrategy)
            }

            LaunchedEffect(key1 = Unit) {
                saveStateHandler?.onSaveStateHandling(
                    androidVm.viewModelScope,
                    androidVm.savedStateHandle,
                    androidVm.emaViewModel
                )
            }
            onViewModelInstance?.invoke(vm)
            val screenToDraw = @Composable {
                EmaComposableScreen(
                    initializer = initializer,
                    onNavigationEvent = onNavigationEvent,
                    onBackEvent = onBackEvent,
                    vm = vm,
                    actions = vmActions,
                    screenContent = screenContent,
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
            route = parseRouteWithInitializerSupport(routeId),
            arguments = listOf(navArgument(EmaInitializer.KEY) {
                initializerSupport?.also {
                    type = EmaInitializerNavType(it.serializerStrategy)
                }
                nullable = true
            }),
            enterTransition = transitionAnimation.enterTransition,
            exitTransition = transitionAnimation.exitTransition,
            popEnterTransition = transitionAnimation.popEnterTransition,
            popExitTransition = transitionAnimation.popExitTransition
        ) {
            content(it)
        }
    }
}

fun routeWithInitializer(
    route: String,
    initializerBundle: EmaInitializerBundle?,
) = initializerBundle?.let {
    routeWithInitializer(route, it.initializer, it.serializer)
} ?: route

private fun parseRouteWithInitializerSupport(routeId: String): String {
    return "$routeId?${EmaInitializer.KEY}={${EmaInitializer.KEY}}"
}

fun routeWithInitializer(
    routeId: String,
    initializer: EmaInitializer,
    serializer: BundleSerializerStrategy
): String {
    return "$routeId?${EmaInitializer.KEY}=${serializer.toStringValue(initializer)}"
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


