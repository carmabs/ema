package com.carmabs.ema.android.ui

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.android.viewmodel.EmaViewModelFactory
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.core.view.EmaView
import com.carmabs.ema.core.viewmodel.EmaViewModelBasic


/**
 * View to handle VM view logic states through [EmaState].
 * The user must provide in the constructor by template:
 *  - The view model class [EmaViewModelBasic] is going to use the view
 *  - The navigation state class [EmaNavigationEvent] will handle the navigation
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
interface EmaAndroidView<S : EmaDataState, VM : EmaViewModelBasic<S, D>, D : EmaNavigationEvent> :
    EmaView<S, VM, D> {

    val androidViewModelSeed: EmaAndroidViewModel<S,D>

    fun initializeViewModel(
        activity: ComponentActivity,
    ): VM {
        val emaFactory = EmaViewModelFactory(androidViewModelSeed)
        val vm = ViewModelProvider(
            activity,
            emaFactory
        )[androidViewModelSeed::class.java]


        return vm.emaViewModel as VM
    }

    fun initializeViewModel(
        fragment: Fragment
    ): VM {
        val emaFactory = EmaViewModelFactory(androidViewModelSeed)
        val vm = ViewModelProvider(fragment, emaFactory)[androidViewModelSeed::class.java]
        return vm.emaViewModel as VM
    }
}