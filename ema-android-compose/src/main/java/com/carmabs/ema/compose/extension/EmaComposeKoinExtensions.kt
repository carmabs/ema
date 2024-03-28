package com.carmabs.ema.compose.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent


@Composable
inline fun <reified T> injectDirectRemembered(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
) = remember {
    KoinJavaComponent.getKoin().get<T>(T::class, qualifier = qualifier, parameters = parameters)
}

