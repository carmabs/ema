package com.carmabs.ema.compose.extension

import com.carmabs.ema.core.action.ViewModelEmaAction
import com.carmabs.ema.core.action.EmaActionDispatcher
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaViewModel
import com.carmabs.ema.core.viewmodel.EmaViewModelAction

/**
 * Created by Carlos Mateo Benito on 12/9/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
fun <S:EmaDataState,D:EmaNavigationEvent,A:ViewModelEmaAction>EmaViewModel<S,D>.asViewModelAction():EmaViewModelAction<S, D, A>{
    return (this as? EmaViewModelAction<S, D, A>)
        ?:throw java.lang.IllegalStateException("${this::class} must inherit form EmaViewModelAction class")
}

fun <A:ViewModelEmaAction>EmaViewModel<*,*>.asActionDispatcher():EmaActionDispatcher<A>{
    return (this as? EmaActionDispatcher<A>)
        ?:throw java.lang.IllegalStateException("${this::class} must implement EmaActionDispatcher with the proper action")
}
