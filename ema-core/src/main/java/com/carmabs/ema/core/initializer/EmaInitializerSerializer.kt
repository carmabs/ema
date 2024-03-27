package com.carmabs.ema.core.initializer

/**
 * Created by Carlos Mateo Benito on 19/3/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
interface EmaInitializerSerializer{
    fun save(initializer:EmaInitializer)
    fun restore():EmaInitializer?
}