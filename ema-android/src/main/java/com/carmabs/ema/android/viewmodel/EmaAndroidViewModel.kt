package com.carmabs.ema.android.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
abstract class EmaAndroidViewModel(val emaViewModel:EmaViewModel<*,*>) :
    ViewModel() {

    init {
        Class.forName(emaViewModel.javaClass.name).kotlin.functions.find { it.name == "setScope" }?.javaMethod?.invoke(emaViewModel,viewModelScope)
    }
    @CallSuper
    override fun onCleared() {
        Class.forName(emaViewModel.javaClass.name).kotlin.functions.find { it.name == "onCleared" }?.javaMethod?.invoke(emaViewModel)
        super.onCleared()
    }
}