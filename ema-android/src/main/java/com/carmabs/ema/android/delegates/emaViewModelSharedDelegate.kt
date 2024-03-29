package com.carmabs.ema.android.delegates

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.carmabs.ema.android.ui.EmaAndroidView
import com.carmabs.ema.android.ui.EmaFragment
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaState
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
class emaViewModelSharedDelegate<S : EmaDataState, VM : EmaViewModel<S, N>, N : EmaNavigationEvent>(
    private val viewModelSeed: () -> VM,
    private val observerFunction: ((attachedState: EmaState<S,N>) -> Unit)? = null,

    ) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): VM {
        val emaView = (thisRef as? EmaAndroidView<S, EmaViewModel<S,N>, N>)
            ?: throw IllegalAccessException(
                "You must use this delegate " +
                        "in an object that inherits from EmaView"
            )
        val activity: FragmentActivity = when (thisRef) {
            is Fragment -> thisRef.requireActivity()
            is FragmentActivity -> thisRef
            is View -> thisRef.context as FragmentActivity
            else -> throw IllegalAccessException("The view must be contained inside a FragmentActivity lifecycle")
        }

        return when (emaView) {
            is EmaFragment<*, S,EmaViewModel<S,N>, N> -> {
                emaView.addExtraViewModel(
                    viewModelSeed.invoke(),
                    emaView,
                    activity,
                    observerFunction
                )
            }

            else -> throw IllegalAccessException("The view must be a EmaFragment")
        }
    }
}