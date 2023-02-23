package com.carmabs.ema.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.carmabs.domain.model.User
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.extension.string
import com.carmabs.ema.android.ui.EmaFragment
import com.carmabs.ema.android.ui.recycler.EmaBaseRecyclerAdapter
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.sample.ema.databinding.HomeFragmentBinding


class HomeFragment :
    EmaFragment<HomeFragmentBinding,HomeState, HomeViewModel, HomeDestination>() {

    private var adapter:EmaBaseRecyclerAdapter<User>?=null

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): HomeFragmentBinding {
        return HomeFragmentBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setupListeners()
        binding.apply {
            rvHomeUsers.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            rvHomeUsers.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))
        }
    }

    private fun HomeFragmentBinding.setupListeners() {
        bHomeCreateProfile.setOnClickListener {
            vm.onActionCreateProfileClicked()
        }
    }

    override fun provideAndroidViewModel(): EmaAndroidViewModel {
        return HomeAndroidViewModel(injectDirect())
    }

    override fun HomeFragmentBinding.onStateNormal(data: HomeState){
        if(isFirstNormalExecution){
            adapter = if(data.showFriendList)
                HomeSingleAdapter()
            else
                HomeMultiAdapter()
            rvHomeUsers.adapter = adapter
        }
        adapter?.submitList(data.userList)
        tvHomeListTitle.text = data.listName.string(requireContext())
        bHomeCreateProfile.isVisible = data.showCreateButton
    }

    override val navigator: EmaNavigator<HomeDestination> = HomeNavigator(this)
}
