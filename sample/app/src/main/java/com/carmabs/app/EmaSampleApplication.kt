package com.carmabs.app

import com.carmabs.app.di.dataModule
import com.carmabs.app.di.uiModule
import com.carmabs.app.di.useCaseModule
import com.carmabs.ema.android.base.EmaApplication
import org.koin.core.KoinApplication
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

    override fun KoinApplication.injectAppModules(): List<Module> {
        return listOf(dataModule, uiModule, useCaseModule)
    }
}