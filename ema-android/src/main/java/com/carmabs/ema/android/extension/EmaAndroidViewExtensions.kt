package com.carmabs.ema.android.extension

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.carmabs.ema.android.ui.EmaAndroidView
import com.carmabs.ema.android.ui.EmaFragment
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaViewModel

/**
 * Created by Carlos Mateo Benito on 13/02/2021.
 *
 * <p>
 * Copyright (c) 2021 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
@Suppress("ClassName")
internal fun <S : EmaDataState,VM : EmaViewModel<S, N>, N : EmaNavigationEvent>EmaAndroidView<S,VM,N>.generateViewModel(
    viewModelSeed: VM
): VM {

    val fragmentScope = (this as? EmaFragment<*, *, *, *>)?.fragmentViewModelScope ?: false

    val newVm = if (fragmentScope) {
        val fragment = this as Fragment
        initializeViewModel(fragment,viewModelSeed)
    } else {
        val activity: FragmentActivity = when (this) {
            is Fragment -> requireActivity()
            is FragmentActivity -> this
            is View -> context as FragmentActivity
            else -> throw IllegalAccessException("The view must be contained inside a FragmentActivity lifecycle")
        }
        initializeViewModel(activity,viewModelSeed)
    }

    return newVm
}