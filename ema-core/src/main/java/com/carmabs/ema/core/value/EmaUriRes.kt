package com.carmabs.ema.core.value

/**
 * Created by Carlos Mateo Benito on 2/2/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
@JvmInline
value class EmaUriRes(val value: String)
val String.uriRes
    get() = EmaUriRes(this)