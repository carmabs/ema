package com.carmabs.ema.android.base

import android.app.Application
import com.carmabs.ema.android.di.emaInjectionModule
import com.carmabs.ema.core.concurrency.EmaScopeDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.javaMethod

/**
 *
 * Abstract base class to implement Kodein framework in applicacion context
 * to handle dependency injection
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */
interface EmaApplicationAware {

    fun Application.initializeEma(){
        Class.forName(EmaScopeDispatcher::class.java.name).kotlin.functions.find { it.name == "changeEmaMainDispatcher" }?.javaMethod?.invoke(EmaScopeDispatcher,Dispatchers.Main.immediate)
        startKoin{
            androidContext(this@initializeEma)

            val modulesList = mutableListOf<Module>()
            modulesList.add(emaInjectionModule())
            injectAppModules()?.onEach { module->
                modulesList.add(module)
            }
            modules(modulesList)
        }
    }

    /**
     * The child classes implement this methods to return the module that provides the app scope objects
     * @return The Koin module which makes the injection
     */
    fun KoinApplication.injectAppModules(): List<Module>?
}