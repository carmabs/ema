package com.carmabs.ema.core.action

/**
 * Created by Carlos Mateo Benito on 14/04/2023.
 *
 * Dummy ActionDispatcher used normally for previews in compose.
 * It is used to avoid implement on action when you only need to assign
 * an actionDispatcher that you are not going to use.
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class EmaActionDispatcherEmpty<A : EmaAction>: EmaActionDispatcher<A> {
    override fun onAction(action: A) = Unit
}