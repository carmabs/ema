package com.carmabs.ema.android.extension

import android.content.Context
import com.carmabs.ema.core.constants.INT_ZERO
import com.carmabs.ema.core.model.EmaText

/**
 * Created by Carlos Mateo Benito on 25/12/21.
 *
 * <p>
 * Copyright (c) 2021 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */

/**
 * Transform ema text to string value.
 */
fun EmaText.string(context: Context): String {
    return when (this) {
        is EmaText.Id -> data?.let { context.getString(id, *it) } ?: context.getString(id)
        is EmaText.Plural -> {
            data?.let {
                context.resources.getQuantityString(id, quantity,*it)
            }?: context.resources.getQuantityString(
                    id,
                   quantity
                )
        }
        is EmaText.Text -> data?.let { String.format(text, *it) } ?: text
    }
}