package com.carmabs.ema.presentation.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carmabs.ema.R
import com.carmabs.ema.android.delegates.emaViewModelSharedDelegate
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.extension.getFormattedString
import com.carmabs.ema.android.navigation.EmaAndroidNavigator
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.core.navigator.EmaNavigationState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.databinding.FragmentUserBinding
import com.carmabs.ema.databinding.LayoutEmaHeaderBinding
import com.carmabs.ema.presentation.base.BaseFragment
import com.carmabs.ema.presentation.ui.home.EmaAndroidHomeToolbarViewModel
rect
import org.koin.android.ext.android.inject


/**
 *  *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 *
 * The toolbar title is set up by provideFixedToolbarTitle() method
 * Use the EmaTheme because it isn,t overriden by parent activity
 * Use the EmaRecycleAdapter with multiple layout in same RecyclerView
 * Transition animation set in navigation_ema_home
 * Add extra view model to get access to EmaHomeToolbarViewModel (View model can be shared if it
 * is attached to activity scope)
 */
class EmaUserFragment :
    BaseFragment<FragmentUserBinding, EmaUserState, EmaUserViewModel, EmaNavigationState>() {

    override val navigator: EmaAndroidNavigator<EmaNavigationState>? = null

    override fun provideAndroidViewModel(): EmaAndroidViewModel<EmaUserViewModel> {
        return injectDirect()
    }

    private val toolbarViewModel: EmaAndroidHomeToolbarViewModel by emaViewModelSharedDelegate(
        {
            di.direct.instance()
        }
    )

    private lateinit var adapter: EmaUserAdapter

    val bindingHeader
        get() = LayoutEmaHeaderBinding.bind(binding.lUserEmaHeader.root)

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUserBinding {
        return FragmentUserBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.toolbarViewModel = toolbarViewModel.emaViewModel
        setupRecycler(vm)
    }

    private fun setupRecycler(viewModel: EmaUserViewModel) {
        adapter = EmaUserAdapter().apply {
            setOnItemClickListener { index, item ->
                viewModel.onActionUserClicked(item)
            }
        }
        binding.rvUser.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvUser.adapter = adapter
    }

    override val viewModelSeed: EmaUserViewModel by inject()

    override fun FragmentUserBinding.onNormal(data: EmaUserState) {
        bindingHeader.tvUserName.text = data.name
        bindingHeader.tvUserSurname.text = data.surname
        adapter.updateList(data.itemList)
    }

    override fun FragmentUserBinding.onSingle(data: EmaExtraData) {

        when (data.type) {
            EmaUserViewModel.SINGLE_EVENT_USER -> {
                val itemLeft = data.extraData as? EmaUserLeftModel
                itemLeft?.also {
                    Toast.makeText(
                        requireContext(),
                        R.string.user_hello_user.getFormattedString(
                            requireContext(),
                            itemLeft.name
                        ),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            EmaUserViewModel.SINGLE_EVENT_GROUP -> {
                val itemRight = data.extraData as? EmaUserRightModel
                itemRight?.also {
                    Toast.makeText(
                        requireContext(),
                        R.string.user_hello_group.getFormattedString(
                            requireContext(),
                            itemRight.number
                        ),
                        Toast.LENGTH_SHORT
                    )
                        .show()

                }
            }
        }
    }
}
