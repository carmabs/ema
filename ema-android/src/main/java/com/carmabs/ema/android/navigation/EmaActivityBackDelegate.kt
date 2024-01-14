package com.carmabs.ema.android.navigation

import com.carmabs.ema.core.model.EmaBackHandlerStrategy

/**
 * Created by Carlos Mateo Benito on 12/1/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
interface EmaActivityBackDelegate {

    val ownsBackDelegate:Boolean
    fun onBackDelegate():EmaBackHandlerStrategy
}