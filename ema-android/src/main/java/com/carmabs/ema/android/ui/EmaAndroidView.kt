package com.carmabs.ema.android.ui

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.carmabs.ema.android.initializer.savestate.SaveStateManager
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.android.viewmodel.EmaViewModelFactory
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.core.view.EmaView
import com.carmabs.ema.core.viewmodel.EmaViewModel


/**
 * View to handle VM view logic states through [EmaState].
 * The user must provide in the constructor by template:
 *  - The view model class [EmaViewModel] is going to use the view
 *  - The navigation state class [EmaNavigationEvent] will handle the navigation
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
interface EmaAndroidView<S : EmaDataState, VM : EmaViewModel<S, N>, N : EmaNavigationEvent> :
    EmaView<S, VM, N> {

    fun initializeViewModel(
        activity: ComponentActivity,
        viewModelSeed: VM
    ): EmaAndroidViewModel<S,N> {
        val emaFactory = EmaViewModelFactory(viewModelSeed)
        val vm = ViewModelProvider(
            activity,
            emaFactory
        )[viewModelSeed.id, EmaAndroidViewModel::class.java]
        return vm as EmaAndroidViewModel<S, N>
    }

    fun initializeViewModel(
        fragment: Fragment,
        viewModelSeed: VM
    ): EmaAndroidViewModel<S,N> {
        val emaFactory = EmaViewModelFactory(viewModelSeed)
        val vm = ViewModelProvider(
            fragment,
            emaFactory
        )[viewModelSeed.id, EmaAndroidViewModel::class.java]
        return vm as EmaAndroidViewModel<S, N>
    }
}