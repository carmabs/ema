package com.carmabs.ema.core.model

/**
 * Created by Carlos Mateo Benito on 10/11/23 for EMA.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com.com”>Carlos Mateo Benito</a>
 */
sealed interface EmaBackHandlerStrategy {
    class ContinueOnBackPressed(val removeBackHandler:Boolean = true): EmaBackHandlerStrategy
    object Cancelled: EmaBackHandlerStrategy
}