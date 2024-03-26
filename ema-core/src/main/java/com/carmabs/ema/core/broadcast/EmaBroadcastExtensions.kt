package com.carmabs.ema.core.broadcast

import com.carmabs.ema.core.viewmodel.EmaViewModel
import kotlin.reflect.KClass

/**
 * Created by Carlos Mateo Benito on 6/3/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
val <T : EmaViewModel<*, *>> KClass<T>.backBroadcastId: BackBroadcastId
    get() = BackBroadcastId("broadcast/${this.java.name}")

@JvmInline
value class BackBroadcastId internal constructor(val id: String)