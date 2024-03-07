package com.carmabs.ema.presentation.ui.login

import com.carmabs.domain.manager.ResourceManager
import com.carmabs.domain.model.LoginRequest
import com.carmabs.domain.model.Role
import com.carmabs.domain.model.User
import com.carmabs.domain.usecase.LoginUseCase
import com.carmabs.ema.core.broadcast.broadcastId
import com.carmabs.ema.core.constants.STRING_EMPTY
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.presentation.base.BaseViewModel
import com.carmabs.ema.presentation.dialog.error.ErrorDialogData
import com.carmabs.ema.presentation.dialog.error.ErrorDialogListener
import com.carmabs.ema.presentation.ui.home.HomeInitializer
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationViewModel

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val resourceManager: ResourceManager,
    initialDataState: LoginState
) : BaseViewModel<LoginState, LoginAction, LoginNavigationEvent>(initialDataState) {

    companion object {
        const val EVENT_MESSAGE = "EVENT_MESSAGE"
        const val EVENT_LAST_USER_ADDED = "EVENT_LAST_USER_ADDED"
    }

    override fun onStateCreated(initializer: EmaInitializer?) = Unit
    override fun onAction(action: LoginAction) {
        when(action){
            LoginAction.DeleteUser -> {
                onActionDeleteUser()
            }
            LoginAction.Login -> {
                onActionLogin()
            }
            is LoginAction.PasswordWritten -> {
                onActionPasswordWrite(action.password)
            }
            is LoginAction.UserNameWritten -> {
                onActionUserWrite(action.user)
            }
        }
    }

    private var pendingUser: User? = null

    override fun onBroadcastListenerSetup() {
        //When two or more resultReceived WITH THE SAME CODE are active, for example in this case,
        //this receiver and the EmaHomeToolbarViewModel receiver, only the last one is executed.
        //ActivityCreated -> EmaHomeToolbarViewModel added -> Fragment created -> EEmaHomeViewModel added->
        //only this result received is executed.
        addOnBroadcastListener(ProfileCreationViewModel::class.broadcastId) {
            pendingUser = it as User

        }
    }

    override fun onViewResumed() {
        super.onViewResumed()
        pendingUser?.also {
            notifySingleEvent(EmaExtraData(EVENT_LAST_USER_ADDED, data = it))
        }
        pendingUser = null
    }

    private fun doLogin() {
        sideEffect {
            showLoading()
            val user =
                loginUseCase.invoke(LoginRequest(stateData.userName, stateData.userPassword))
            updateToNormalState()
            notifySingleEvent(
                EmaExtraData(
                    EVENT_MESSAGE,
                    resourceManager.getCongratulations(user.name)
                )
            )
            val userType = when (user.role) {
                Role.ADMIN -> LoginNavigationEvent.LoginSuccess.UserType.Admin(
                    user = user
                )

                Role.BASIC ->
                    LoginNavigationEvent.LoginSuccess.UserType.Basic
            }
            navigate(LoginNavigationEvent.LoginSuccess(userType))

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
            stateData.userName.isEmpty() -> showError(ErrorDialogData(
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

            stateData.userPassword.isEmpty() -> showError(ErrorDialogData(
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
        updateState {
            copy(userName = user)
        }
    }

    fun onActionPasswordWrite(password: String) {
        updateState {
            copy(userPassword = password)
        }
    }

}
