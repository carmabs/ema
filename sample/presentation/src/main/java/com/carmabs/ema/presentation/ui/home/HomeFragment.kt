package com.carmabs.ema.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.ui.EmaFragment
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.sample.ema.databinding.HomeFragmentBinding


class HomeFragment :
    EmaFragment<HomeFragmentBinding,HomeState, HomeViewModel, HomeDestination>() {

    private val adapter = HomeMultiAdapter()

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): HomeFragmentBinding {
        return HomeFragmentBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvHomeUsers.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            rvHomeUsers.adapter = adapter
        }
    }

    override fun provideAndroidViewModel(): EmaAndroidViewModel {
        return HomeAndroidViewModel(injectDirect())
    }

    override fun HomeFragmentBinding.onStateNormal(data: HomeState){
        adapter.submitList(data.userList)
    }

    override val navigator: EmaNavigator<HomeDestination> = HomeNavigator(this)
}
