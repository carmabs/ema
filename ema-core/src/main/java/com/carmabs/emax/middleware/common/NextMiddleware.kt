package com.carmabs.emax.middleware.common

import com.carmabs.ema.core.action.EmaAction

/**
 * Created by Carlos Mateo Benito on 4/10/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
typealias NextMiddleware = (EmaAction) -> Unit
