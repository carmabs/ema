package com.carmabs.ema.presentation.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.carmabs.domain.model.Role
import com.carmabs.domain.model.User
import com.carmabs.ema.android.ui.recycler.EmaMultiRecyclerAdapter
import com.carmabs.ema.android.ui.recycler.EmaViewHolder
import com.carmabs.ema.sample.ema.R
import com.carmabs.ema.sample.ema.databinding.HomeLayoutItemAdminBinding
import com.carmabs.ema.sample.ema.databinding.HomeLayoutItemUserBinding

class HomeMultiAdapter : EmaMultiRecyclerAdapter<User>() {

    override fun ViewBinding.bind(
        item: User,
        viewType: Int,
        holder: EmaViewHolder<User>,
        payloads: MutableList<Any>
    ) {
       when(Role.entries[viewType]){
           Role.ADMIN -> {
               (this as HomeLayoutItemAdminBinding).apply {
                   tvHomeItemAdmin.text = "${item.name} ${item.surname}"

               }
           }
           Role.BASIC -> {
               (this as HomeLayoutItemUserBinding).apply {
                   tvHomeItemUser.text = item.name
                   tvHomeItemUserSurname.text = item.surname
               }
           }
       }
    }

    override fun createMultiViewHolder(view: ViewGroup, viewType: Int): EmaAdapterMultiViewHolder {
       val viewBinding =  when(Role.entries[viewType]){
            Role.ADMIN -> HomeLayoutItemAdminBinding.inflate(LayoutInflater.from(view.context),view,false)
            Role.BASIC -> HomeLayoutItemUserBinding.inflate(LayoutInflater.from(view.context),view,false)
        }

        return EmaAdapterMultiViewHolder(viewBinding,viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].role.ordinal
    }
}