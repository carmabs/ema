package com.carmabs.ema.presentation.ui.login

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.ui.EmaFragment
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.presentation.base.BaseFragment
import com.carmabs.ema.presentation.ui.home.EmaHomeViewModel
import com.carmabs.ema.sample.ema.databinding.FragmentHomeBinding
import com.carmabs.ema.sample.ema.databinding.LoginFragmentBinding


class LoginFragment :
    BaseFragment<LoginFragmentBinding,LoginState, LoginViewModel, LoginDestination>() {

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): LoginFragmentBinding {
        return LoginFragmentBinding.inflate(inflater,container,false)
    }

    override fun provideAndroidViewModel(): EmaAndroidViewModel {
        return LoginAndroidViewModel(injectDirect())
    }

    override fun LoginFragmentBinding.onStateNormal(data: LoginState){
    
    }


    override fun LoginFragmentBinding.onOverlayed(data: EmaExtraData) {
        showLoadingDialog()
    }

    override fun LoginFragmentBinding.onSingle(data: EmaExtraData) {
        when (data.type) {
            EmaHomeViewModel.EVENT_MESSAGE -> Toast.makeText(
                requireContext(),
                data.extraData as String,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override val navigator: EmaNavigator<LoginDestination> = LoginNavigator(this)
}
