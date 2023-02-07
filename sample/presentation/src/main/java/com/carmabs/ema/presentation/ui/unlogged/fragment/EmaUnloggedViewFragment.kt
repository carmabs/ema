package com.carmabs.ema.presentation.ui.unlogged.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carmabs.ema.R
import com.carmabs.ema.android.delegates.emaViewModelSharedDelegate
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.databinding.FragmentUnloggedBinding
import com.carmabs.ema.presentation.base.BaseFragment
import com.carmabs.ema.presentation.ui.unlogged.EmaAndroidUnloggedToolbarViewModel
import com.carmabs.ema.presentation.ui.unlogged.EmaAndroidUnloggedViewModel
import com.carmabs.ema.presentation.ui.unlogged.EmaUnloggedNavigator
import com.carmabs.ema.presentation.ui.unlogged.EmaUnloggedState
import com.carmabs.ema.presentation.ui.unlogged.EmaUnloggedToolbarState
import com.carmabs.ema.presentation.ui.unlogged.EmaUnloggedViewModel
import org.koin.android.ext.android.inject

/**
 * Fragment for unlogged toolbar
 *
 *
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

class EmaUnloggedViewFragment :
    BaseFragment<FragmentUnloggedBinding,EmaUnloggedState, EmaUnloggedViewModel, EmaUnloggedNavigator.Navigation>() {

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUnloggedBinding {
        return FragmentUnloggedBinding.inflate(inflater,container,false)
    }

    /**
     * If you wouldn't want to use dependency injection you can provide it instantiating the class.
     * Not recommended
     */
    private val toolbarViewModel: EmaAndroidUnloggedToolbarViewModel by emaViewModelSharedDelegate(
        {
           EmaAndroidUnloggedToolbarViewModel(injectDirect())
        }
    ) {
        vm.onToolbarUpdated(it as EmaState<EmaUnloggedToolbarState>)
    }

    override val navigator: EmaUnloggedNavigator by inject()

    override fun provideAndroidViewModel(): EmaAndroidViewModel<EmaUnloggedViewModel> {
        return EmaAndroidUnloggedViewModel(injectDirect())
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons(vm)
    }

    override fun onStart() {
        super.onStart()
        vm.toolbarViewModel = toolbarViewModel.emaViewModel
    }

    private fun setupButtons(viewModel: EmaUnloggedViewModel) {
        binding.bErrorToolbar.setOnClickListener { viewModel.onActionToolbar() }
        binding.bErrorAddUser.setOnClickListener { viewModel.onActionAddUser() }
    }

    override fun FragmentUnloggedBinding.onNormal(data: EmaUnloggedState) {
        checkShowToolbarTriggerVisibility(data)
    }

    private fun checkShowToolbarTriggerVisibility(data: EmaUnloggedState) {
        binding.bErrorToolbar.visibility =
            if (data.showToolbarViewVisibility) View.VISIBLE else View.GONE
    }
}