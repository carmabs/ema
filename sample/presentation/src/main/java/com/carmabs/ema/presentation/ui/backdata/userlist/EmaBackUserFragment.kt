package com.carmabs.ema.presentation.ui.backdata.userlist;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.extension.checkVisibility
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.databinding.FragmentBackBinding
import com.carmabs.ema.presentation.base.BaseFragment
import com.carmabs.ema.presentation.ui.backdata.EmaBackNavigator
import org.koin.android.ext.android.inject

/**
 *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 * <p>
 * Date: 2019-11-07
 */

class EmaBackUserFragment : BaseFragment<FragmentBackBinding,EmaBackUserState, EmaBackUserViewModel, EmaBackNavigator.Navigation>() {

    private val adapter : EmaBackUserAdapter by lazy { EmaBackUserAdapter() }

    override fun provideAndroidViewModel(): EmaAndroidViewModel<EmaBackUserViewModel> {
        return injectDirect()
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBackBinding {
        return FragmentBackBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupButton(vm)
    }

    private fun setupButton(viewModel: EmaBackUserViewModel) {
        binding.bBack.setOnClickListener {
            viewModel.onActionAddUser()
        }
    }

    private fun setupRecycler() {
        binding.rvBack.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        binding.rvBack.adapter = adapter
    }


    override val navigator: EmaBackNavigator by inject()

    override fun FragmentBackBinding.onNormal(data: EmaBackUserState) {
        adapter.updateList(data.listUsers)
        tvBackNoUsers.visibility = checkVisibility(data.noUserVisibility)
        rvBack.visibility = checkVisibility(!data.noUserVisibility, gone = false)
    }
}
