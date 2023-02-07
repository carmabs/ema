package com.carmabs.ema.presentation.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.carmabs.domain.exception.LoginException
import com.carmabs.domain.exception.PasswordEmptyException
import com.carmabs.domain.exception.UserEmptyException
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.extension.checkUpdate
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.core.constants.STRING_EMPTY
import com.carmabs.ema.core.dialog.EmaDialogProvider
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.presentation.DIALOG_TAG_LOADING
import com.carmabs.ema.presentation.base.BaseFragment
import com.carmabs.ema.presentation.dialog.loading.LoadingDialogData
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogData
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogListener
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogProvider
import com.carmabs.ema.sample.ema.databinding.FragmentHomeBinding
import org.koin.android.ext.android.inject

/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 *
 * Created by: Carlos Mateo Benito on 20/1/19.
 *
 * Use of bindForUpdate and checkUpdate
 * Use of ReceiverListener
 */
class EmaHomeFragment : BaseFragment<FragmentHomeBinding, EmaHomeState, EmaHomeViewModel, EmaHomeNavigator.Navigation>() {


    override fun provideAndroidViewModel(): EmaAndroidViewModel<EmaHomeViewModel> {
        return injectDirect()
    }

    override val navigator: EmaHomeNavigator by inject()

    //As we can see we can use an instance by kodein or generate it by class instance
    private val errorDialog: EmaDialogProvider by lazy { SimpleDialogProvider(childFragmentManager) }

    private val loadingDialog: EmaDialogProvider by inject(tag = DIALOG_TAG_LOADING)
    /////////////////////////////////////////////////////////////////////////////////


    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialog(vm)
        setupButtons(vm)
    }

    private fun setupDialog(viewModel: EmaHomeViewModel) {
        errorDialog.dialogListener = object : SimpleDialogListener {
            override fun onCancelClicked() {
                viewModel.onActionDialogErrorCancel()
            }

            override fun onBackPressed() {
                viewModel.onActionDialogErrorCancel()
            }

            override fun onConfirmClicked() {
                viewModel.onActionDialogErrorAccept()
            }

        }
    }

    private val bindingUser
        get() = LayoutUserBinding.bind(binding.layoutLightLoginUser.root)
    private val bindingPassword
        get() = LayoutPasswordBinding.bind(binding.layoutLightLoginPassword.root)

    private fun setupButtons(viewModel: EmaHomeViewModel) {
        binding.swLightLoginRememberPassword.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onActionRemember(
                isChecked
            )
        }

        bindingUser.ivHomeTouchEmptyUser.setOnClickListener { viewModel.onActionDeleteUser() }
        bindingPassword.ivHomePassEmptyPassword.setOnClickListener { viewModel.onActionDeletePassword() }
        bindingPassword.ivHomePassSeePassword.setOnClickListener { viewModel.onActionShowPassword() }
        bindingUser.etUser.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                //Always the text is changed it has to be notified to ViewModel to update its state
                viewModel.onActionUserWrite(s?.toString() ?: STRING_EMPTY)
            }
        })
        bindingPassword.etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                //Always the text is changed it has to be notified to ViewModel to update its state
                viewModel.onActionPasswordWrite(s?.toString() ?: STRING_EMPTY)
            }

        })
        binding.bLightLoginSign.setOnClickListener {
            viewModel.onActionLogin()
        }
    }

    private fun checkError(textView: TextView) {
        textView.visibility = View.VISIBLE
    }

    private fun FragmentHomeBinding.hideErrors() {
        tvLightLoginErrorUser.visibility = View.GONE
        tvLightLoginErrorPassword.visibility = View.GONE
        errorDialog.hide()
    }

    private fun hideLoading() {
        loadingDialog.hide()
    }

    private fun showErrorDialog() {
        errorDialog.show(
            SimpleDialogData(
                getString(R.string.home_invalid_credentials_title),
                getString(R.string.home_invalid_credentials_message)
            )
        )
    }

    private fun showLoadingDialog() {
        loadingDialog.show(
            LoadingDialogData(
                getString(R.string.home_loading_title),
                getString(R.string.home_loading_title_message)
            )
        )
    }


    override fun FragmentHomeBinding.onNormal(data: EmaHomeState) {

        //We hide the dialogs in normal state
        hideErrors()
        hideLoading()


        //////THIS TWO METHODS ARE SIMILAR, USE IT DEPENDING YOUR USE CASE/////

        //Use this to check if values are different, this way you avoid to call ViewModel
        //due to TextWatcher and the chance to generate infinite loop:
        //Example : etUser change s
        // -> TextWatcher calls viewmodel
        // -> Viewmodel update the view
        // -> etUser set the data.userName
        // -> TextWatcher calls viewmodel
        // -> ¡INFINITE LOOP!
        //
        checkUpdate(bindingUser.etUser.text.toString(), data.userName) {
            bindingUser.etUser.setText(data.userName)
        }

        //Use this to execute the set view value operation only if the selected state property has been
        //updated.
        //This could be very use to execute animations on views, this way the animation only will be executed
        //when the value has been updated, not everytime state view is updated.


        bindForUpdate(data::userPassword) {
            bindingPassword.etPassword.setText(data.userPassword)
        }

        //////////////////////////////////////////////////////////////////////

        swLightLoginRememberPassword.isChecked = data.rememberUser

        data.showPassword.let {
            if (it) {
                bindingPassword.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            } else {
                bindingPassword.etPassword.transformationMethod = null

            }
            bindingPassword.etPassword.setSelection(bindingPassword.etPassword.text.length)
        }
    }

    override fun FragmentHomeBinding.onOverlayed(data: EmaExtraData) {
        showLoadingDialog()
    }

    override fun FragmentHomeBinding.onSingle(data: EmaExtraData) {
        when (data.type) {
            EmaHomeViewModel.EVENT_MESSAGE -> Toast.makeText(
                requireContext(),
                data.extraData as String,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun FragmentHomeBinding.onError(error: Throwable) {
        when (error) {
            is UserEmptyException -> checkError(tvLightLoginErrorUser)
            is PasswordEmptyException -> checkError(tvLightLoginErrorPassword)
            is LoginException -> showErrorDialog()
        }
    }
}