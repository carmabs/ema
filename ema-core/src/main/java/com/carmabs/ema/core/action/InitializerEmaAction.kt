package com.carmabs.ema.core.action

/**
 * Created by Carlos Mateo Benito on 1/10/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
interface InitializerEmaAction : EmaAction{
    override val type: String
        get() = InitializerEmaAction::class.java.simpleName
}
