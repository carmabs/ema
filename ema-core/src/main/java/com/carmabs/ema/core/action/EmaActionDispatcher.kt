package com.carmabs.ema.core.action

import kotlinx.coroutines.flow.Flow

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


interface EmaActionDispatcher<A : EmaAction> {
    fun dispatch(action: A)

    fun subscribeToActions(): Flow<A>
}