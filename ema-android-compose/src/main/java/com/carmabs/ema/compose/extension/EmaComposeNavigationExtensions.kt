package com.carmabs.ema.compose.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import com.carmabs.ema.android.extension.findActivity
import com.carmabs.ema.android.navigation.EmaEmptyNavigator
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.compose.provider.EmaScreenProvider
import com.carmabs.ema.compose.ui.EmaComposableScreen
import com.carmabs.ema.compose.ui.EmaComposableScreenContent
import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.action.EmaActionDispatcher
import com.carmabs.ema.core.constants.INT_ZERO
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.navigator.EmaEmptyDestination
import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaViewModel


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

fun <S : EmaDataState, D : EmaDestination, A : EmaAction> NavGraphBuilder.createComposableScreen(
    defaultState: S,
    screenContent: EmaComposableScreenContent<S, A>,
    navigator: @Composable ((NavBackStackEntry) -> EmaNavigator<D>),
    routeId: String = screenContent::class.routeId(),
    overrideInitializer: EmaInitializer? = null,
    androidViewModel: @Composable () -> EmaAndroidViewModel,
) {
    composable(routeId) { navBack ->
        val vm = EmaScreenProvider().provideComposableViewModel(androidViewModel = androidViewModel.invoke())
        EmaComposableScreen(
            initializer = overrideInitializer ?: getAndReleaseInitializer(routeId),
            defaultState = defaultState,
            navigator = navigator.invoke(navBack),
            vm = (vm as EmaViewModel<S, D>),
            actions = (vm as? EmaActionDispatcher<A>)
                ?: throw java.lang.IllegalStateException("${vm::class} must implement EmaActionDispatcher the proper action"),
            screenContent = screenContent
        )
    }
}

fun <S : EmaDataState, A : EmaAction> NavGraphBuilder.createComposableScreen(
    defaultState: S,
    screenContent: EmaComposableScreenContent<S, A>,
    navController: NavController,
    routeId: String = screenContent::class.routeId(),
    overrideInitializer: EmaInitializer? = null,
    androidViewModel: @Composable () -> EmaAndroidViewModel,
) {
    composable(routeId) {
        val vm = EmaScreenProvider().provideComposableViewModel(androidViewModel = androidViewModel.invoke())
        EmaComposableScreen(
            initializer = overrideInitializer ?: getAndReleaseInitializer(routeId),
            defaultState = defaultState,
            navigator = EmaEmptyNavigator(
                LocalContext.current.findActivity(),
                navController
            ),
            vm = vm as EmaViewModel<S, EmaEmptyDestination>,
            actions = (vm as? EmaActionDispatcher<A>)
                ?: throw java.lang.IllegalStateException("${vm::class} must implement EmaActionDispatcher the proper action"),
            screenContent = screenContent
        )
    }
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
