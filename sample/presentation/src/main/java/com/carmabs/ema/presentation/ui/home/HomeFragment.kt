package com.carmabs.ema.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.carmabs.domain.model.Role
import com.carmabs.domain.model.User
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.extension.getFormattedString
import com.carmabs.ema.android.initializer.bundle.strategy.BundleSerializerStrategy
import com.carmabs.ema.android.ui.EmaFragment
import com.carmabs.ema.android.ui.recycler.EmaBaseRecyclerAdapter
import com.carmabs.ema.core.constants.STRING_EMPTY
import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.sample.ema.R
import com.carmabs.ema.sample.ema.databinding.HomeFragmentBinding


class HomeFragment :
    EmaFragment<HomeFragmentBinding, HomeState, HomeViewModel, HomeNavigationEvent>() {

    private var adapter: EmaBaseRecyclerAdapter<User>? = null
    override val initializerStrategy: BundleSerializerStrategy
        get() = BundleSerializerStrategy.kSerialization(HomeInitializer.serializer())

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): HomeFragmentBinding {
        return HomeFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setupListeners()
        binding.apply {
            rvHomeUsers.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvHomeUsers.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun HomeFragmentBinding.setupListeners() {
        bHomeCreateProfile.setOnClickListener {
            viewModel.dispatch(HomeAction.ProfileClicked)
        }
    }


    override fun provideViewModel(): HomeViewModel {
        return injectDirect()
    }

    override fun HomeFragmentBinding.onStateNormal(data: HomeState) {
        if (data.showAdminList) {
            if (adapter == null) {
                adapter = when (data.userData?.role) {
                    Role.ADMIN -> HomeMultiAdapter()
                    Role.BASIC -> HomeSingleAdapter()
                    null -> null
                }
                rvHomeUsers.adapter = adapter
            }
            adapter?.submitList(data.userList)
        }
        tvHomeListTitle.text = when (val user = data.userData) {
            is HomeState.UserData.Admin -> {
                R.string.home_admin_title.getFormattedString(
                    requireContext(),
                    user.name,
                    user.surname
                )
            }

            HomeState.UserData.Basic -> {
                R.string.home_user_title.getFormattedString(requireContext())
            }

            null -> STRING_EMPTY
        }

        bHomeCreateProfile.isVisible = data.showCreateButton
    }

    override val navigator: EmaNavigator<HomeNavigationEvent> = HomeNavigator(this)
}
