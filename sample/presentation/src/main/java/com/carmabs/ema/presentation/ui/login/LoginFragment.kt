package com.carmabs.ema.presentation.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.carmabs.domain.model.User
import com.carmabs.ema.android.base.EmaSingleToast
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.extension.getFormattedString
import com.carmabs.ema.android.extension.setTextWithCursorAtEnd
import com.carmabs.ema.android.extension.string
import com.carmabs.ema.core.constants.STRING_EMPTY
import com.carmabs.ema.core.extension.toEmaText
import com.carmabs.ema.core.model.EmaText
import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.presentation.base.BaseFragment
import com.carmabs.ema.presentation.dialog.error.ErrorDialogData
import com.carmabs.ema.presentation.dialog.error.ErrorDialogListener
import com.carmabs.ema.sample.ema.R
import com.carmabs.ema.sample.ema.databinding.LoginFragmentBinding


class LoginFragment :
    BaseFragment<LoginFragmentBinding, LoginState, LoginViewModel, LoginNavigationEvent>() {

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): LoginFragmentBinding {
        return LoginFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setupListeners()
    }

    private fun LoginFragmentBinding.setupListeners() {
        layoutLoginUser.etUser.addTextChangedListener {
            viewModel.onActionUserWrite(it?.toString() ?: STRING_EMPTY)
        }
        layoutLoginPassword.etPassword.addTextChangedListener {
            viewModel.onActionPasswordWrite(it?.toString() ?: STRING_EMPTY)
        }
        bLoginSign.setOnClickListener {
            viewModel.onActionLogin()
        }
        layoutLoginUser.ivHomeTouchEmptyUser.setOnClickListener {
            viewModel.onActionDeleteUser()
        }
    }

    override fun LoginFragmentBinding.onOverlappedError(extraData: EmaExtraData) {
        when (extraData.data as LoginOverlap) {
            LoginOverlap.ErrorBadCredentials -> {
                showError(ErrorDialogData(
                    EmaText.id(R.string.general_error_title),
                    EmaText.id(R.string.login_error_fail),
                ), object : ErrorDialogListener {
                    override fun onConfirmClicked() {
                        viewModel.onAction(LoginAction.Error.BadCredentialsAccepted)
                    }

                    override fun onBackPressed() {
                        viewModel.onAction(LoginAction.Error.BackPressed)
                    }

                })
            }

            LoginOverlap.ErrorUserEmpty -> {
                showError(ErrorDialogData(
                    EmaText.id(R.string.general_error_title),
                    EmaText.id(R.string.login_error_user_empty),
                ), object : ErrorDialogListener {
                    override fun onConfirmClicked() {
                        viewModel.onAction(LoginAction.Error.UserEmptyAccepted)
                    }

                    override fun onBackPressed() {
                        viewModel.onAction(LoginAction.Error.BackPressed)
                    }

                })
            }

            LoginOverlap.ErrorPasswordEmpty -> {
                showError(ErrorDialogData(
                    EmaText.id(R.string.general_error_title),
                    EmaText.id(R.string.login_error_password_empty),
                ), object : ErrorDialogListener {
                    override fun onConfirmClicked() {
                        viewModel.onAction(LoginAction.Error.PasswordEmptyAccepted)
                    }

                    override fun onBackPressed() {
                        viewModel.onAction(LoginAction.Error.BackPressed)
                    }

                })
            }

        }

    }

    override fun provideViewModel(): LoginViewModel {
        return injectDirect()
    }

    override fun LoginFragmentBinding.onNormal(data: LoginState) {
        bindForUpdate(data::userName) {
            layoutLoginUser.etUser.setTextWithCursorAtEnd(data.userName)
        }
        bindForUpdate(data::userPassword) {
            layoutLoginPassword.etPassword.setTextWithCursorAtEnd(data.userPassword)
        }
    }


    override fun LoginFragmentBinding.onSingle(extra: EmaExtraData) {
        when (val event = extra.data as LoginSingleEvent) {
            is LoginSingleEvent.LastUserAdded -> {
                val user = event.user
                EmaSingleToast.show(
                    requireContext(),
                    R.string.login_last_user_added.getFormattedString(
                        requireContext(),
                        "${user.name} ${user.surname})"
                    ),
                    Toast.LENGTH_SHORT
                )
            }

            is LoginSingleEvent.Message -> {
                EmaSingleToast.show(
                    requireContext(),
                    R.string.home_welcome.getFormattedString(requireContext(),event.userName),
                    Toast.LENGTH_SHORT
                )

            }
        }
    }

    override val navigator: EmaNavigator<LoginNavigationEvent> = LoginNavigator(this)
}
