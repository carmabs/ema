package com.carmabs.ema.core.action

import com.carmabs.ema.core.constants.INT_ONE
import com.carmabs.ema.core.viewmodel.EmaViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Created by Carlos Mateo Benito on 14/04/2023.
 *
 * Interface to dispatch actions generated in views. It should be use
 * to abstract actions and implement it in viewmodel. It is based on MVI
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */


class DefaultEmaActionDispatcher<A : EmaAction>(private val dispatchAction:(A)->Unit) : EmaActionDispatcher<A>  {
    private val actionFlow = MutableSharedFlow<A>(extraBufferCapacity = INT_ONE, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override fun dispatch(action: A){
        dispatchAction(action)
        actionFlow.tryEmit(action)
    }

    override fun subscribeToActions(): Flow<A>{
        return actionFlow
    }
}