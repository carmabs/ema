package com.carmabs.ema.android.viewmodel

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.carmabs.ema.core.viewmodel.EmaViewModel


/**
 * Interface which all factories of [EmaAndroidViewModel] must use to be handled by the library
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
abstract class EmaFactory<VM : EmaViewModel<*, *>> : AbstractSavedStateViewModelFactory() {

    /**
     * Called if the view model has not been created yet
     * @param T View model class
     * @param modelClass Class of the view model
     */
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return EmaAndroidViewModel(createViewModel(), provideSavedStateHandle() ?: handle) as T
    }

    abstract fun createViewModel(): VM

    abstract fun provideSavedStateHandle(): SavedStateHandle?
}