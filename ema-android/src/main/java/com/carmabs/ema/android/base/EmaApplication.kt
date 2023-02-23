package com.carmabs.ema.android.base

import android.app.Application
import androidx.annotation.CallSuper

/**
 *
 * Abstract base class to implement Kodein framework in applicacion context
 * to handle dependency injection
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */
abstract class EmaApplication : Application(), EmaApplicationAware {

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        initializeEma()
    }
}