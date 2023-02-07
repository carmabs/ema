package com.carmabs.ema.presentation.ui.backdata.creation

import com.carmabs.domain.manager.ResourceManager
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.presentation.base.BaseViewModel
import com.carmabs.ema.presentation.ui.backdata.EmaBackNavigator
import com.carmabs.ema.presentation.ui.backdata.userlist.EmaBackUserModel

/**
 *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 *
 * Date: 2019-11-07
 */

class EmaBackUserCreationViewModel(private val resourceManager: ResourceManager) : BaseViewModel<EmaBackUserCreationState, EmaBackNavigator.Navigation>() {

    companion object {
        const val RESULT_USER = 0
    }

    override val initialViewState: EmaBackUserCreationState = EmaBackUserCreationState()


    override fun onStartFirstTime(statePreloaded: Boolean) {
        updateToOverlayedState()
    }

    fun onActionAddUser(name: String, surname: String) {
        when {
            name.isEmpty() -> notifySingleEvent(EmaExtraData(extraData = resourceManager.getResultErrorFillName()))
            surname.isEmpty() -> notifySingleEvent(EmaExtraData(extraData = resourceManager.getResultErrorFillSurname()))
            else -> {
                addResult(EmaBackUserModel(
                        name = name,
                        surname = surname
                ), RESULT_USER)
                addResult(Pair(name, System.currentTimeMillis()))
                navigateBack()
            }
        }
    }

    fun onActionNameWrite(name: String) {
        updateToNormalState {
            copy(name = name)
        }
    }

    fun onActionSurnameWrite(surname: String) {
        updateToNormalState {
            copy(surname = surname)
        }
    }
}