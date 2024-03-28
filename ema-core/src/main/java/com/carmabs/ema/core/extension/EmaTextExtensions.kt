package com.carmabs.ema.core.extension

import com.carmabs.ema.core.model.EmaText

/**
 * Created by Carlos Mateo Benito on 23/3/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
fun String.toEmaText(): EmaText {
    return EmaText.text(this)
}
fun String.toEmaText(vararg data:Any): EmaText {
    return EmaText.text(this)
}