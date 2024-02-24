package com.carmabs.ema.android.savestate

import androidx.lifecycle.SavedStateHandle
import com.carmabs.ema.core.initializer.EmaInitializer

/**
 * Created by Carlos Mateo Benito on 16/2/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.comm”>Carlos Mateo Benito</a>
 */
interface InitializerRepository {
   fun save(arcInitializer: EmaInitializer,savedStateHandle: SavedStateHandle)
   fun restore(savedStateHandle: SavedStateHandle): EmaInitializer
}