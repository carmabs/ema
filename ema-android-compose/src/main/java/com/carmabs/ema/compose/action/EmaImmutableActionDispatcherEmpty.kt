package com.carmabs.ema.compose.action

import com.carmabs.ema.core.action.ViewModelEmaAction
import com.carmabs.ema.core.action.EmaActionDispatcherEmpty

/**
 * Created by Carlos Mateo Benito on 16/7/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
fun <A : ViewModelEmaAction>EmaImmutableActionDispatcherEmpty() = EmaActionDispatcherEmpty<A>().toImmutable()
