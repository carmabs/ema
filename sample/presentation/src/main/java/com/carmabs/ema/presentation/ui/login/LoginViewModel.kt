package com.carmabs.ema.presentation.ui.login

import com.carmabs.domain.manager.ResourceManager
import com.carmabs.domain.model.LoginRequest
import com.carmabs.domain.model.Role
import com.carmabs.domain.model.User
import com.carmabs.domain.usecase.LoginUseCase
import com.carmabs.ema.core.constants.STRING_EMPTY
import com.carmabs.ema.core.extension.resultId
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.presentation.base.BaseViewModel
import com.carmabs.ema.presentation.dialog.error.ErrorDialogData
import com.carmabs.ema.presentation.dialog.error.ErrorDialogListener
import com.carmabs.ema.presentation.ui.home.HomeInitializer
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationViewModel

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val resourceManager: ResourceManager
) : BaseViewModel<LoginState, LoginDestination>() {

    companion object {
        const val EVENT_MESSAGE = "EVENT_MESSAGE"
        const val EVENT_LAST_USER_ADDED = "EVENT_LAST_USER_ADDED"
    }

    override suspend fun onCreateState(initializer: EmaInitializer?): LoginState {
        return LoginState()
    }

    private var pendingUser:User?=null
    override fun onResultListenerSetup() {
        //When two or more resultReceived WITH THE SAME CODE are active, for example in this case,
        //this receiver and the EmaHomeToolbarViewModel receiver, only the last one is executed.
        //ActivityCreated -> EmaHomeToolbarViewModel added -> Fragment created -> EEmaHomeViewModel added->
        //only this result received is executed.

        addOnResultListener(ProfileCreationViewModel::class.resultId()){
            pendingUser  = it as User

        }

    }

    override fun onViewResumed() {
        super.onViewResumed()
        pendingUser?.also{
            notifySingleEvent(EmaExtraData(EVENT_LAST_USER_ADDED,data = it))
        }
        pendingUser = null
    }

    private fun doLogin() {
        executeUseCase {
            showLoading()
            val user =
                loginUseCase.execute(LoginRequest(stateData.userName, stateData.userPassword))
            updateToNormalState()
            notifySingleEvent(
                EmaExtraData(
                    EVENT_MESSAGE,
                    resourceManager.getCongratulations(user.name)
                )
            )
            val initializerHome = when (user.role) {
                Role.ADMIN -> HomeInitializer.Admin(
                    admin = user
                )
                Role.BASIC ->
                    HomeInitializer.BasicUser
            }
            navigate(LoginDestination.Home(initializerHome))

        }.onError {
            showError(ErrorDialogData(
                resourceManager.getErrorTitle(),
                resourceManager.getErrorLogin()
            ),
                object : ErrorDialogListener {
                    override fun onConfirmClicked() {
                        updateToNormalState()
                    }

                    override fun onBackPressed() {
                        updateToNormalState()
                    }

                })
        }
    }

    fun onActionLogin() {
        when {
            stateData.userName.isEmpty() ->  showError(ErrorDialogData(
                resourceManager.getErrorTitle(),
                resourceManager.getErrorLoginUserEmpty()
            ),
                object : ErrorDialogListener {
                    override fun onConfirmClicked() {
                        updateToNormalState()
                    }

                    override fun onBackPressed() {
                        updateToNormalState()
                    }

                })
            stateData.userPassword.isEmpty() ->  showError(ErrorDialogData(
                resourceManager.getErrorTitle(),
                resourceManager.getErrorLoginPasswordEmpty()
            ),
                object : ErrorDialogListener {
                    override fun onConfirmClicked() {
                        updateToNormalState()
                    }

                    override fun onBackPressed() {
                        updateToNormalState()
                    }

                })
            else -> doLogin()
        }
    }

    fun onActionDeleteUser() {
        updateToNormalState {
            copy(
                userName = STRING_EMPTY,
                userPassword = STRING_EMPTY
            )
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

}
