package com.carmabs.ema.core.selector

/**
 * Created by Carlos Mateo Benito on 12/03/2021.
 *
 * Class used to handle the unique selection of a list of items if any of them has been selected and deselect the other ones.
 * <p>
 * Copyright (c) 2021 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class EmaUniqueSelector<T>(private val listOptions : List<T>,private val selectionListener: OnSelectionListener<T>) {

    fun select(option:T){
        listOptions.forEach {
            if(option!=it){
                selectionListener.onUnselected(it)
            }
        }
        selectionListener.onSelected(option)
    }

    interface OnSelectionListener<T>{
        fun onSelected(option:T)
        fun onUnselected(option:T)
    }
}