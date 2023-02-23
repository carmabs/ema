package com.carmabs.ema.core.state

/**
 * Interface which all state view classes must use to be handled by the library
 *
 * The states mast have all properties as val to make it inmutable for views. If it has to be change
 * use the copy methods of data classes inside the ViewModel
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

interface EmaDataState {
    fun checkIsValidStateDataClass() = this is EmaEmptyState || this::class.isData
}