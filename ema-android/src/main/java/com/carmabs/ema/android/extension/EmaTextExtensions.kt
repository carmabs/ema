package com.carmabs.ema.android.extension

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import com.carmabs.ema.core.constants.STRING_EMPTY
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
                context.resources.getQuantityString(id, quantity, *it)
            } ?: context.resources.getQuantityString(
                id,
                quantity
            )
        }

        is EmaText.Text -> data?.let { String.format(text, *it) } ?: text
        is EmaText.Composition -> {
            texts.fold(STRING_EMPTY) { acc, emaText ->
                acc + emaText.string(context)
            }
        }
    }
}
fun @receiver:StringRes Int.toEmaText(vararg data: Any): EmaText {
    return EmaText.id(id = this, data = data)
}
fun @receiver:PluralsRes Int.toEmaText(quantity: Int, vararg data: Any): EmaText {
    return EmaText.plural(id = this, quantity = quantity, data = data)
}