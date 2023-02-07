package com.carmabs.ema.presentation.ui.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.carmabs.ema.R
import com.carmabs.ema.android.extension.getFormattedString
import com.carmabs.ema.android.ui.EmaMultiRecyclerAdapter
import com.carmabs.ema.android.ui.EmaViewHolder
import com.carmabs.ema.databinding.ItemLeftBinding
import com.carmabs.ema.databinding.ItemRightBinding

/**
 *
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 *
 * Date: 2019-09-25
 */

class EmaUserAdapter :
    EmaMultiRecyclerAdapter<EmaUserItemModel>() {


    override fun getItemViewType(position: Int): Int {
        return currentList[position].type.id
    }

    /////////////////////////////////////////////////////////////////////////
    // SAMPLE CODE IF YOU WANT TO USE A MULTIVIEW IN ADAPTER OR CUSTOM VIEW//
    /////////////////////////////////////////////////////////////////////////

    override fun createMultiViewHolder(view: ViewGroup, viewType: Int): EmaAdapterMultiViewHolder {
        return when (EmaUserItemModel.getFromId(viewType)) {
            EmaUserItemModel.Type.LEFT -> EmaAdapterMultiViewHolder(
                ItemLeftBinding.inflate(
                    LayoutInflater.from(view.context), view, false
                ), viewType
            )
            EmaUserItemModel.Type.RIGHT -> EmaAdapterMultiViewHolder(
                ItemRightBinding.inflate(
                    LayoutInflater.from(view.context), view, false
                ), viewType
            )
        }
    }

    override fun ViewBinding.bind(
        item: EmaUserItemModel,
        viewType: Int,
        holder: EmaViewHolder<EmaUserItemModel>,
        payloads: MutableList<Any>
    ) {
        when (EmaUserItemModel.getFromId(viewType)) {
            EmaUserItemModel.Type.LEFT -> {
                this as ItemLeftBinding
                val leftItem = item as EmaUserLeftModel
                tvItemLeft.text = R.string.user_name.getFormattedString(root.context, leftItem.name)
            }

            EmaUserItemModel.Type.RIGHT -> {
                this as ItemRightBinding
                val rightItem = item as EmaUserRightModel
                tvItemRight.text =
                    R.string.user_number_people.getFormattedString(root.context, rightItem.number)
            }
        }
    }
}