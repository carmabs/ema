package com.carmabs.ema.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import com.carmabs.ema.android.base.EmaCoreFragment
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.viewmodel.EmaViewModel


/**
 *
 * Abstract base class to implement ViewBinding in fragment
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */
abstract class EmaFragment<B : ViewBinding, S : EmaDataState, VM : EmaViewModel<S, D>, D : EmaNavigationEvent> :
    EmaCoreFragment<S,VM,D>(){

    protected var isFirstNormalExecution: Boolean = true
        private set

    protected var isFirstOverlayedExecution: Boolean = true
        private set

    private var _binding: B? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    protected val binding
        get() = _binding!!

    /**
     * Method to provide the fragment ViewBinding class to represent the layout.
     */
    abstract fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): B


    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        isFirstNormalExecution = true
        isFirstOverlayedExecution = true
        _binding = createViewBinding(inflater, container)
        return binding.root
    }



    @CallSuper
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    final override fun onEmaStateNormal(data: S) {
        binding.onStateNormal(data)
        isFirstNormalExecution = false
    }

    final override fun onEmaStateOverlapped(extra: EmaExtraData) {
        binding.onStateOverlapped(extra)
        isFirstOverlayedExecution = false
    }

    final override fun onSingleEvent(extra: EmaExtraData) {
        binding.onSingleEvent(extra)
    }

    abstract fun B.onStateNormal(data: S)
    protected open fun B.onStateOverlapped(extra: EmaExtraData) {}
    protected open fun B.onSingleEvent(extra: EmaExtraData) {}

}