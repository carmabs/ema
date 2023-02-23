package com.carmabs.ema.compose.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.pluralStringResource
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
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EmaText.stringResource(): String {
    return when (this) {
        is EmaText.Id -> data?.let { androidx.compose.ui.res.stringResource(id, *it) } ?: androidx.compose.ui.res.stringResource(id)
        is EmaText.Plural -> {
            data?.let {
                pluralStringResource(id, quantity,*it)
            }?: pluralStringResource(
                    id,
                   quantity
                )
        }
        is EmaText.Text -> data?.let { String.format(text, *it) } ?: text
    }
}