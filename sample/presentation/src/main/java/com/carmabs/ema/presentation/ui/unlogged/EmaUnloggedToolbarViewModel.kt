package com.carmabs.ema.presentation.ui.unlogged

import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.presentation.base.BaseViewModel
import com.carmabs.ema.presentation.ui.backdata.userlist.EmaBackUserViewModel
import com.carmabs.ema.presentation.ui.unlogged.EmaUnloggedNavigator
import com.carmabs.ema.presentation.ui.unlogged.EmaUnloggedToolbarState

/**
 *
 *
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

class EmaUnloggedToolbarViewModel : BaseViewModel<EmaUnloggedToolbarState, EmaUnloggedNavigator.Navigation>() {

    override val initialViewState: EmaUnloggedToolbarState = EmaUnloggedToolbarState()

    override fun onStartFirstTime(statePreloaded: Boolean) {

    }

    override fun onResultListenerSetup() {
        addOnResultReceived(EmaBackUserViewModel.RESULT_USER_NUMBER) {
            notifySingleEvent(EmaExtraData(extraData = it.data as Int))
        }
    }

    fun onActionMenuHideToolbar() {
        hideToolbar()
    }

    fun showToolbar() {
        updateToNormalState {
            copy(visibility = true)
        }
    }

    fun hideToolbar() {
        updateToNormalState {
            copy(visibility = false)
        }
    }
}