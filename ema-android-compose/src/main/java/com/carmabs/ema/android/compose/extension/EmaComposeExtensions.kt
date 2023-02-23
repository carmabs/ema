package com.carmabs.ema.android.compose.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.carmabs.ema.android.compose.ui.EmaComposableScreenContent
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent
import kotlin.reflect.KClass


fun <T : EmaComposableScreenContent<*, *>> KClass<T>.routeId(): String {
    return "screen/${this.java.name}/${this.hashCode()}"
}

@Composable
inline fun <reified T> injectDirectRemembered(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
) = remember {
    KoinJavaComponent.getKoin().get<T>(T::class, qualifier = qualifier, parameters = parameters)
}

