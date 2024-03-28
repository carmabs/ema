package com.carmabs.ema.core.extension

import com.carmabs.ema.core.constants.STRING_EMPTY
import com.carmabs.ema.core.model.EmaText

/**
 * Created by Carlos Mateo Benito on 2019-11-24.
 *
 * <p>
 * Copyright (c) 2019 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */

fun String?.checkNull(defaultValue: String = STRING_EMPTY): String {
    return this ?: defaultValue
}

fun String?.checkNullOrEmpty(defaultValue: String = STRING_EMPTY): String {
    return if(isNullOrEmpty())defaultValue else this
}

fun String.getFormattedString(vararg data: Any?): String {
    return String.format(this, *data)
}

fun String.replaceLast(delimiter: Char, newString: String): String {
    val startString = substringBeforeLast(delimiter).trim()
    val endString = substringAfterLast(delimiter).trim()
    return startString + newString + endString
}
