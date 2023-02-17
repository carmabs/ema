package com.carmabs.ema.presentation.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.core.constants.STRING_EMPTY
import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.presentation.base.BaseFragment
import com.carmabs.ema.sample.ema.databinding.LoginFragmentBinding


class LoginFragment :
    BaseFragment<LoginFragmentBinding,LoginState, LoginViewModel, LoginDestination>() {

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): LoginFragmentBinding {
        return LoginFragmentBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            layoutLoginUser.etUser.addTextChangedListener {
                vm.onActionUserWrite(it?.toString()?: STRING_EMPTY)
            }
            layoutLoginPassword.etPassword.addTextChangedListener {
                vm.onActionPasswordWrite(it?.toString()?: STRING_EMPTY)
            }
            swLoginRememberPassword.setOnCheckedChangeListener { buttonView, isChecked ->
                vm.onActionRemember(isChecked)
            }
            bLoginSign.setOnClickListener {
                vm.onActionLogin()
            }
        }
    }

    override fun provideAndroidViewModel(): EmaAndroidViewModel {
        return LoginAndroidViewModel(injectDirect())
    }

    override fun LoginFragmentBinding.onNormal(data: LoginState){
    
    }


    override fun LoginFragmentBinding.onOverlayed(data: EmaExtraData) {
        when(data.type){
            LoginViewModel.OVERALAYED_LOADING->{
                //showLoadingDialog()
            }
        }

    }

    override fun LoginFragmentBinding.onSingle(data: EmaExtraData) {
        when (data.type) {
            LoginViewModel.EVENT_MESSAGE -> Toast.makeText(
                requireContext(),
                data.extraData as String,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override val navigator: EmaNavigator<LoginDestination> = LoginNavigator(this)
}
