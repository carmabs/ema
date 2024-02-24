package com.carmabs.ema.presentation.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.carmabs.domain.model.User
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.extension.getFormattedString
import com.carmabs.ema.android.extension.setTextWithCursorAtEnd
import com.carmabs.ema.android.extension.string
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.core.constants.STRING_EMPTY
import com.carmabs.ema.core.model.EmaText
import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.presentation.base.BaseFragment
import com.carmabs.ema.sample.ema.R
import com.carmabs.ema.sample.ema.databinding.LoginFragmentBinding


class LoginFragment :
    BaseFragment<LoginFragmentBinding, LoginState, LoginViewModel, LoginDestination>() {

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
            vm.onActionUserWrite(it?.toString() ?: STRING_EMPTY)
        }
        layoutLoginPassword.etPassword.addTextChangedListener {
            vm.onActionPasswordWrite(it?.toString() ?: STRING_EMPTY)
        }
        bLoginSign.setOnClickListener {
            vm.onActionLogin()
        }
        layoutLoginUser.ivHomeTouchEmptyUser.setOnClickListener {
            vm.onActionDeleteUser()
        }
    }

    override fun provideViewModel(): EmaViewModel {
        return LoginAndroidViewModel(injectDirect())
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
        when (extra.id) {
            LoginViewModel.EVENT_MESSAGE -> Toast.makeText(
                requireContext(),
                (extra.data as EmaText).string(requireContext()),
                Toast.LENGTH_LONG
            ).show()
            LoginViewModel.EVENT_LAST_USER_ADDED -> {
                val user = extra.data as User
                Toast.makeText(
                    requireContext(),
                    R.string.login_last_user_added.getFormattedString(
                        requireContext(),
                        "${user.name} ${user.surname})"
                    ),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override val navigator: EmaNavigator<LoginDestination> = LoginNavigator(this)
}
