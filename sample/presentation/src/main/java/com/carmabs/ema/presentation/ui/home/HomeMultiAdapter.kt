package com.carmabs.ema.presentation.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.carmabs.domain.model.Role
import com.carmabs.domain.model.User
import com.carmabs.ema.android.ui.recycler.EmaMultiRecyclerAdapter
import com.carmabs.ema.android.ui.recycler.EmaViewHolder
import com.carmabs.ema.sample.ema.databinding.HomeLayoutItemAdminBinding
import com.carmabs.ema.sample.ema.databinding.HomeLayoutItemUserBinding

class HomeMultiAdapter : EmaMultiRecyclerAdapter<User>() {

    override fun ViewBinding.bind(
        item: User,
        viewType: Int,
        holder: EmaViewHolder<User>,
        payloads: MutableList<Any>
    ) {
       when(Role.values()[viewType]){
           Role.ADMIN -> {
               (this as HomeLayoutItemAdminBinding).apply {

               }
           }
           Role.USER -> {
               (this as HomeLayoutItemUserBinding).apply {

               }
           }
       }
    }

    override fun createMultiViewHolder(view: ViewGroup, viewType: Int): EmaAdapterMultiViewHolder {
       val viewBinding =  when(Role.values()[viewType]){
            Role.ADMIN -> HomeLayoutItemAdminBinding.inflate(LayoutInflater.from(view.context))
            Role.USER -> HomeLayoutItemUserBinding.inflate(LayoutInflater.from(view.context))
        }

        return EmaAdapterMultiViewHolder(viewBinding,viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].role.ordinal
    }
}