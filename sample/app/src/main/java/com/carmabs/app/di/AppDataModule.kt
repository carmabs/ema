package com.carmabs.app


import com.carmabs.data.manager.AndroidResourceManager
import com.carmabs.data.repository.MockRepository
import com.carmabs.domain.repository.Repository
import com.carmabs.domain.usecase.LoginUseCase
import org.koin.dsl.module

/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 *
 * Created by: Carlos Mateo Benito on 20/1/19.
 */

fun appDataInjection() = module {

   single<Repository> {
      MockRepository()
   }

   single { LoginUseCase(get()) }

   single { AndroidResourceManager(get()) }
}