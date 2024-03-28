package com.carmabs.ema.compose.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.carmabs.ema.compose.extension.activity
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.navigator.EmaNavigationNode

@Composable
fun <D : EmaNavigationEvent> rememberEmaNodeNavigator(
    navHostController: NavHostController,
    navigatorProvider: (Activity, NavHostController) -> EmaComposableNodeNavigator<D>
): EmaComposableNodeNavigator<D> {
    val activity = LocalContext.activity
    val navigator = remember {
        navigatorProvider.invoke(activity, navHostController)
    }
    return navigator
}

abstract class EmaComposableNodeNavigator<D : EmaNavigationEvent>(
    activity: Activity,
    navController: NavHostController
) : EmaComposableNavigator(activity, navController) {

    private var previousNavigationNode: EmaNavigationNode<D>? = null

    fun navigate(navigationNode: EmaNavigationNode<D>,onNavigated:((D)->Unit)?=null) {
        if (navigationNode.id != previousNavigationNode?.id) {
            val destination = navigationNode.value
            if (previousNavigationNode?.hasPreviousNode(node = navigationNode) == true) {
                previousNavigationNode = navigationNode
                navigateBack()
            } else {
                previousNavigationNode = navigationNode
                onNavigation(destination)
                onNavigated?.invoke(destination)
            }
        }
    }

    abstract fun onNavigation(navigationEvent: D): Boolean
}