package com.carmabs.ema.android.delegates

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.carmabs.ema.android.ui.EmaAndroidView
import com.carmabs.ema.android.ui.EmaFragment
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaViewModel
import kotlin.reflect.KProperty

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
class emaViewModelDelegate<S : EmaDataState, D : EmaNavigationEvent, VM : EmaViewModel<S, D>> {

    private var vm: VM? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): VM {

        return vm ?: let {
            val emaView = (thisRef as? EmaAndroidView<S, VM, D>) ?: throw IllegalAccessException(
                "You must use this delegate " +
                        "in an object that inherits from EmaView"
            )

            val fragmentScope = (emaView as? EmaFragment<*, *, *, *>)?.fragmentViewModelScope ?: false

            val newVm = if (fragmentScope) {
                val fragment = emaView as Fragment
                emaView.initializeViewModel(fragment)
            } else {
                val activity: FragmentActivity = when (emaView) {
                    is Fragment -> emaView.requireActivity()
                    is FragmentActivity -> emaView
                    is View -> emaView.context as FragmentActivity
                    else -> throw IllegalAccessException("The view must be contained inside a FragmentActivity lifecycle")
                }
                emaView.initializeViewModel(activity)
            }

            return newVm.apply {
                vm = this
            }
        }
    }
}