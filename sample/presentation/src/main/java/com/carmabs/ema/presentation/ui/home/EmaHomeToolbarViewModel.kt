package com.carmabs.ema.presentation.ui.home

import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.presentation.base.BaseViewModel

/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 *
 * Created by: Carlos Mateo Benito on 20/1/19.
 */
class EmaHomeToolbarViewModel : BaseViewModel<EmaHomeToolbarState, EmaHomeNavigator.Navigation>() {

    override fun onStartFirstTime(statePreloaded: Boolean) {

    }

    override fun onResultListenerSetup() {
       addOnResultReceived{
           (it.data as? Pair<*, *>)?.also { pair ->
               notifySingleEvent(EmaExtraData(extraData = pair))
           }
       }
    }

    fun setToolbarTitle(title:String?){
        updateToNormalState {
            copy(toolbarTitle = title)
        }
    }

    override val initialViewState: EmaHomeToolbarState = EmaHomeToolbarState()
}