package com.carmabs.ema.presentation.ui.login

import com.carmabs.domain.manager.ResourceManager
import com.carmabs.domain.model.LoginRequest
import com.carmabs.domain.usecase.LoginUseCase
import com.carmabs.ema.core.constants.STRING_EMPTY
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.viewmodel.EmaViewModel
import com.carmabs.ema.presentation.base.BaseViewModel
import com.carmabs.ema.presentation.ui.home.HomeInitializer

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val resourceManager: ResourceManager
) : BaseViewModel<LoginState, LoginDestination>() {

    companion object {
        const val EVENT_MESSAGE = 1000
        const val OVERLAYED_EMPTY = 1001
        const val OVERLAYED_ERROR = 1002
        const val OVERALAYED_LOADING = 1003
    }

    override suspend fun onCreateState(initializer: EmaInitializer?): LoginState {
        return LoginState()
    }

    override fun onResultListenerSetup() {
        //When two or more resultReceived WITH THE SAME CODE are active, for example in this case,
        //this receiver and the EmaHomeToolbarViewModel receiver, only the last one is executed.
        //ActivityCreated -> EmaHomeToolbarViewModel added -> Fragment created -> EEmaHomeViewModel added->
        //only this result received is executed.


        /* Uncomment to test it

        addOnResultReceived{
            (it.data as? Pair<*, *>)?.also { pair ->
                notifySingleEvent(EmaExtraData(extraData = pair))
            }
        }

        */
    }

    private fun doLogin() {
        executeUseCase {
            updateToOverlayedState()
            val user =
                loginUseCase.execute(LoginRequest(stateData.userName, stateData.userPassword))
            updateToNormalState()
            notifySingleEvent(EmaExtraData(EVENT_MESSAGE, resourceManager.getCongratulations()))
             navigate(
                 LoginDestination.Home(
                     HomeInitializer.Default(
                         name = user.name,
                         surname = user.surname
                     )
                 )
             )
        }.onError {
            updateToOverlayedState(EmaExtraData(type = OVERLAYED_ERROR))
        }
    }

    fun onActionLogin() {
            when {
                stateData.userName.isEmpty() -> updateToOverlayedState(EmaExtraData(OVERLAYED_EMPTY))
                stateData.userPassword.isEmpty() -> updateToOverlayedState(EmaExtraData(OVERLAYED_EMPTY))
                else -> doLogin()
            }
    }

    fun onActionShowPassword() {
        updateToNormalState {
            copy(showPassword = !showPassword)
        }
    }

    fun onActionRemember(isChecked: Boolean) {
        updateDataState {
            copy(rememberUser = isChecked)
        }
    }

    fun onActionDeletePassword() {
        updateToNormalState {
            copy(userPassword = STRING_EMPTY)
        }
    }

    fun onActionDeleteUser() {
        updateDataState {
            copy(userName = STRING_EMPTY)
        }
    }


    fun onActionUserWrite(user: String) {
        //We only want to update the data of the view without notifying it, it has the edit text updated with
        //text when you write on it, but you need to save the state if for example, there is a device
        //rotation and the view is recreated, to set the text with last value saved on state

        updateDataState {
            copy(userName = user)
        }
    }

    fun onActionPasswordWrite(password: String) {
        updateDataState {
            copy(userPassword = password)
        }
    }

    fun onActionDialogErrorCancel() {
        updateToNormalState()
    }

    fun onActionDialogErrorAccept() {
        updateToNormalState()
    }

}
