package com.carmabs.app

import com.carmabs.ema.android.base.EmaApplication
import org.koin.core.module.Module


/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 *
 * Created by: Carlos Mateo Benito on 21/1/19.
 */
class EmaSampleApplication : EmaApplication() {

    override fun injectAppModules(): List<Module> {
        return listOf(appDataInjection())
    }
}