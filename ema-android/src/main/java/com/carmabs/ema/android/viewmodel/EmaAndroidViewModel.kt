package com.carmabs.ema.android.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaViewModel
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.javaMethod

/**
 * Created by Carlos Mateo Benito on 13/02/2021.
 *
 * <p>
 * Copyright (c) 2021 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
open class EmaAndroidViewModel<S:EmaDataState,N:EmaNavigationEvent>(
    val emaViewModel:EmaViewModel<S,N>,
    val savedStateHandle: SavedStateHandle = SavedStateHandle()
) : ViewModel() {

    init {
        emaViewModel.setScope(viewModelScope)
    }
    @CallSuper
    override fun onCleared() {
       emaViewModel.onCleared()
        super.onCleared()
    }
}

