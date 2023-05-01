package com.carmabs.ema.core.extension

/**
 * Created by Carlos Mateo Benito on 2023-01-05.
 *
 * <p>
 * Copyright (c) 2019 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */

inline fun Boolean.ifTrue(action: () -> Unit):Boolean {
    if (this)
        action.invoke()
    return this
}

inline fun Boolean.ifFalse(action: () -> Unit):Boolean {
    if (!this)
        action.invoke()
    return this
}



