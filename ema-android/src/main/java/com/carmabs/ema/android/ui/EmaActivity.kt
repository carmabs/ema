package com.carmabs.ema.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import com.carmabs.ema.android.base.EmaCoreActivity
import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.viewmodel.EmaViewModel

/**
 *
 * Abstract base class to implement Kodein framework in activity context
 * to handle dependency injection
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */
abstract class EmaActivity<B : ViewBinding, S : EmaDataState, VM : EmaViewModel<S, D>, D : EmaDestination> :
    EmaCoreActivity<S, VM, D>() {

    protected lateinit var binding: B

    /**
     * Method to provide the activity ViewBinding class to represent the layout.
     */
    abstract fun createViewBinding(inflater: LayoutInflater): B


    /**
     * The onCreate base will set the view specified in [.getLayout] and will
     * inject dependencies and views.
     *
     */
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = createViewBinding(layoutInflater)
        setContentView(binding.root)
    }

    /**
     * Determines first execution for each one of the state methods. EmaView determines when to set it to false.
     */
    protected var isFirstNormalExecution: Boolean = true
        private set

    protected var isFirstOverlayedExecution: Boolean = true
        private set

    final override fun onEmaStateNormal(data: S) {
        binding.onStateNormal(data)
        isFirstNormalExecution = false
    }

    final override fun onEmaStateOverlapped(extra: EmaExtraData) {
        binding.onStateOverlayed(extra)
        isFirstOverlayedExecution = false
    }

    final override fun onSingleEvent(extra: EmaExtraData) {
        binding.onSingleEvent(extra)
    }

    abstract fun B.onStateNormal(data: S)
    protected open fun B.onStateOverlayed(data: EmaExtraData) {}
    protected open fun B.onSingleEvent(data: EmaExtraData) {}
}