package com.carmabs.ema.core.model

import com.carmabs.ema.core.state.EmaExtraData

/**
 * Created by Carlos Mateo Benito on 17/9/23.
 *
 * <p>
 * Copyright (c) 2022 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
sealed class EmaEvent {
    data class Launched(val data: EmaExtraData = EmaExtraData()) : EmaEvent()
    object Consumed : EmaEvent()

    fun isConsumed(): Boolean {
        return this is Consumed
    }
}

inline fun EmaEvent.onLaunched(action: (data:EmaExtraData) -> Unit) {
    if (this is EmaEvent.Launched) {
        action(data)
    }
}

inline fun EmaEvent.onConsumed(action: () -> Unit) {
    if (this.isConsumed()) {
        action()
    }
}