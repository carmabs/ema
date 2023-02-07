package com.carmabs.ema.presentation.ui.backdata.creation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.core.extension.checkNull
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.databinding.FragmentBackResultBinding
import com.carmabs.ema.presentation.base.BaseFragment
import com.carmabs.ema.presentation.ui.backdata.EmaBackNavigator
import org.koin.android.ext.android.inject

/**
 *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 *
 * Date: 2019-11-07
 *
 * Use of EmaEditText.
 */

class EmaBackUserCreationFragment : BaseFragment<FragmentBackResultBinding,EmaBackUserCreationState, EmaBackUserCreationViewModel, EmaBackNavigator.Navigation>() {

    override fun provideAndroidViewModel(): EmaAndroidViewModel<EmaBackUserCreationViewModel> {
        return injectDirect()
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBackResultBinding {
        return FragmentBackResultBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons(vm)
        setupEditTexts(vm)
    }

    private fun setupEditTexts(viewModel: EmaBackUserCreationViewModel) {

        ///With EmaEditText is not necessary the following code to add a textwatcher to update the
        ///state in viewmodel

        /*
        etBackResultSurname.addTextChangedListener(object : TextWatcher {
               override fun afterTextChanged(s: Editable?) {
               }

               override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
               }

               override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                   viewModel.onActionSurnameWrite(s?.toString() ?: STRING_EMPTY)
               }
        })*/

        ///Use the next code ///

        binding.etBackResultName.setEmaTextWatcherListener {
            viewModel.onActionNameWrite(it.checkNull())
        }
        binding.etBackResultSurname.setEmaTextWatcherListener {
            viewModel.onActionSurnameWrite(it.checkNull())
        }
    }


    private fun setupButtons(viewModel: EmaBackUserCreationViewModel) {

        binding.bBackResultAccept.setOnClickListener {
            val name = binding.etBackResultName.text.toString()
            val surname = binding.etBackResultSurname.text.toString()
            viewModel.onActionAddUser(
                    name = name,
                    surname = surname
            )
        }
    }

    override val navigator: EmaBackNavigator by inject()

    override fun FragmentBackResultBinding.onNormal(data: EmaBackUserCreationState) {

        ///Using the EmaEditText it handles text updates to avoid infinite loops described in
        ///HomeViewFragment

        etBackResultName.setText(data.name)
        etBackResultSurname.setText(data.surname)
    }


    override fun FragmentBackResultBinding.onSingle(data: EmaExtraData) {
        Toast.makeText(requireContext(), data.extraData as String, Toast.LENGTH_SHORT).show()
    }
}