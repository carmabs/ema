package com.carmabs.ema.presentation.ui.backdata.userlist

import android.view.LayoutInflater
import android.view.ViewGroup
import com.carmabs.ema.android.ui.EmaRecyclerAdapter
import com.carmabs.ema.android.ui.EmaViewHolder
import com.carmabs.ema.databinding.ItemBackUserBinding

/**
 *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 *
 * Date: 2019-11-07
 */

class EmaBackUserAdapter : EmaRecyclerAdapter<ItemBackUserBinding,EmaBackUserModel>() {

    override fun ItemBackUserBinding.bind(
        item: EmaBackUserModel,
        viewType: Int,
        holder: EmaViewHolder<EmaBackUserModel>,
        payloads: MutableList<Any>
    ) {
        tvItemUserName.text = item.name
        tvItemUserSurname.text = item.surname
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ItemBackUserBinding {
        return ItemBackUserBinding.inflate(inflater,container,false)
    }
}