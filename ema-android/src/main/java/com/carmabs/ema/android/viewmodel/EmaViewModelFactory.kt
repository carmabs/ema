package com.carmabs.ema.android.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.carmabs.ema.core.viewmodel.EmaViewModel

/**
 * Class to wrap the  view model seed for first instance creation.
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
class EmaViewModelFactory<VM : EmaViewModel<*, *>>(
    private val viewModel: VM,
    private val savedStateHandle: SavedStateHandle? = null
) : EmaFactory<VM>() {
    /**
     * @return View model instance
     */
    override fun createViewModel(): VM {
        return viewModel
    }

    override fun provideSavedStateHandle(): SavedStateHandle? {
        return savedStateHandle
    }
}
