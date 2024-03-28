package com.carmabs.ema.presentation.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.carmabs.domain.model.User
import com.carmabs.ema.android.ui.recycler.EmaRecyclerAdapter
import com.carmabs.ema.android.ui.recycler.EmaViewHolder
import com.carmabs.ema.sample.ema.databinding.HomeLayoutItemUserBinding

class HomeSingleAdapter : EmaRecyclerAdapter<HomeLayoutItemUserBinding, User>() {

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): HomeLayoutItemUserBinding {
        return HomeLayoutItemUserBinding.inflate(inflater, container, false)
    }

    override fun HomeLayoutItemUserBinding.bind(
        item: User,
        viewType: Int,
        holder: EmaViewHolder<User>,
        payloads: MutableList<Any>
    ) {
        tvHomeItemUser.text = item.name
        tvHomeItemUserSurname.text = item.surname
    }
}