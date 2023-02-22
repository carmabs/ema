package com.carmabs.ema.android.compose.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import com.carmabs.ema.android.compose.action.EmaComposableScreenActions
import com.carmabs.ema.android.compose.navigation.EmaComposableNavigatorEmpty
import com.carmabs.ema.android.compose.provider.EmaScreenProvider
import com.carmabs.ema.android.compose.ui.EmaComposableScreen
import com.carmabs.ema.android.compose.ui.EmaComposableScreenContent
import com.carmabs.ema.android.extension.findActivity
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
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

fun <S : EmaDataState, VM : EmaViewModel<S, D>, D : EmaDestination, A : EmaComposableScreenActions> NavGraphBuilder.createComposableScreen(
    defaultState: S,
    screenContent: EmaComposableScreenContent<S, A>,
    navigator: @Composable ((NavBackStackEntry) -> EmaNavigator<D>),
    routeId: String = screenContent::class.routeId(),
    overrideInitializer: EmaInitializer? = null,
    androidViewModel: @Composable () -> EmaAndroidViewModel,
) {
    composable(routeId) { navBack ->
        val creator =
            EmaScreenProvider<VM, A>().provide(androidViewModel = androidViewModel.invoke())
        EmaComposableScreen(
            initializer = overrideInitializer?:getAndReleaseInitializer(routeId),
            defaultState = defaultState,
            navigator = navigator.invoke(navBack),
            vm = creator.first,
            screenContent = screenContent,
            actions = creator.second
        )
    }
}

fun <S : EmaDataState, VM : EmaViewModel<S, EmaEmptyDestination>, A : EmaComposableScreenActions> NavGraphBuilder.createComposableScreen(
    defaultState: S,
    screenContent: EmaComposableScreenContent<S, A>,
    navController: NavController,
    routeId: String = screenContent::class.routeId(),
    overrideInitializer: EmaInitializer? = null,
    androidViewModel: @Composable () -> EmaAndroidViewModel,
) {
    composable(routeId) {
        val creator =
            EmaScreenProvider<VM, A>().provide(androidViewModel = androidViewModel.invoke())
        EmaComposableScreen(
            initializer = overrideInitializer?:getAndReleaseInitializer(routeId),
            defaultState = defaultState,
            navigator = EmaComposableNavigatorEmpty(
                LocalContext.current.findActivity(),
                navController
            ),
            vm = creator.first,
            screenContent = screenContent,
            actions = creator.second
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
