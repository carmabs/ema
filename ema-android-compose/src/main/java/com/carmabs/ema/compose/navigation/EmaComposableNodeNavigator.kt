package com.carmabs.ema.compose.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.carmabs.ema.compose.extension.findComponentActivity
import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.navigator.EmaNavigationNode
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.javaMethod

@Composable
fun <D : EmaDestination> rememberEmaNodeNavigator(
    navHostController: NavHostController,
    navigatorProvider: (Activity, NavHostController) -> EmaComposableNodeNavigator<D>
): EmaComposableNodeNavigator<D> {
    val activity = LocalContext.current.findComponentActivity()
    val navigator = remember {
        navigatorProvider.invoke(activity, navHostController)
    }
    return navigator
}

abstract class EmaComposableNodeNavigator<D : EmaDestination>(
    activity: Activity,
    navController: NavHostController,
    closeActivityWhenBackstackIsEmpty: Boolean = true
) : EmaComposableNavigator<D>(activity, navController, closeActivityWhenBackstackIsEmpty) {

    private var previousNavigationNode: EmaNavigationNode<D>? = null

    fun navigate(navigationNode: EmaNavigationNode<D>) {
        if(navigationNode.id != previousNavigationNode?.id) {
            val destination = navigationNode.value
            if (previousNavigationNode?.hasPreviousNode(node = navigationNode) == true) {
                navigateBack()
                previousNavigationNode = navigationNode
            }
            else {
                if (!destination.isNavigated) {
                    val navigated = onNavigation(destination)
                    if (navigated) {
                        Class.forName(destination.javaClass.name).kotlin.functions.find { it.name == "setNavigated" }?.javaMethod?.invoke(
                            destination
                        )
                    }
                    previousNavigationNode = navigationNode
                }
            }
        }
    }

    abstract fun onNavigation(navigationEvent: D): Boolean
}