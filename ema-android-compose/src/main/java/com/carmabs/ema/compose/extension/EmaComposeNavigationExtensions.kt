package com.carmabs.ema.compose.extension

import android.content.Intent
import androidx.compose.runtime.Composable
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
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.compose.action.EmaImmutableActionDispatcher
import com.carmabs.ema.compose.action.toImmutable
import com.carmabs.ema.compose.navigation.EmaComposableTransitions
import com.carmabs.ema.compose.provider.EmaScreenProvider
import com.carmabs.ema.compose.ui.EmaComposableScreen
import com.carmabs.ema.compose.ui.EmaComposableScreenContent
import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.action.EmaActionDispatcher
import com.carmabs.ema.core.constants.INT_ZERO
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.model.EmaBackHandlerStrategy
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaViewModel
import com.carmabs.ema.core.viewmodel.EmaViewModelAction
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

fun <S : EmaDataState, D : EmaNavigationEvent, A : EmaAction.Screen, VM : EmaAndroidViewModel<S, D>> NavGraphBuilder.createComposableScreen(
    screenContent: EmaComposableScreenContent<S, A>,
    onNavigationEvent: (D) -> Unit,
    onBackEvent: ((Any?, EmaImmutableActionDispatcher<A>) -> EmaBackHandlerStrategy)? = null,
    routeId: String = screenContent::class.routeId,
    overrideInitializer: EmaInitializer? = null,
    androidViewModel: @Composable () -> VM,
    onViewModelInstance: (@Composable (EmaViewModel<S, D>) -> Unit)? = null,
    fullScreenDialogMode: Boolean = false,
    transitionAnimation: EmaComposableTransitions = EmaComposableTransitions(),
    decoration: @Composable ((content: @Composable () -> Unit, dispatcher: EmaImmutableActionDispatcher<A>) -> Unit)? = null,
    previewRenderState: S? = null
) {
    val content: @Composable (NavGraphBuilder.(NavBackStackEntry) -> Unit) =
        @Composable { navBack ->
            val viewModel =
                EmaScreenProvider.provideComposableViewModel(androidViewModel = androidViewModel.invoke())
            val vmActions = viewModel.asActionDispatcher<A>().toImmutable()

            onViewModelInstance?.invoke(viewModel)
            val screenToDraw = @Composable {
                EmaComposableScreen(
                    initializer = overrideInitializer ?: getAndReleaseInitializer(routeId),
                    onNavigationEvent = onNavigationEvent,
                    onBackEvent = onBackEvent,
                    vm = viewModel,
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
