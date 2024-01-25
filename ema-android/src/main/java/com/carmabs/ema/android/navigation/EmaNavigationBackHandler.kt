package com.carmabs.ema.android.navigation

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.LifecycleOwner
import com.carmabs.ema.core.model.EmaBackHandlerStrategy

/**
 * Created by Carlos Mateo Benito on 10/11/23 for EMA.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com.com”>Carlos Mateo Benito</a>
 */
class EmaNavigationBackHandler(
    private val activity: ComponentActivity,
    private val lifecycleOwner:LifecycleOwner,
    private val listener: () -> EmaBackHandlerStrategy
) {
    private val backPressedCallback: OnBackPressedCallback
    private var shouldRestore = false

    init {
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (val backHandlingEnabled = listener.invoke()) {
                    is EmaBackHandlerStrategy.ContinueOnBackPressed -> {
                        remove()
                        activity.onBackPressedDispatcher.onBackPressed()
                        if (!backHandlingEnabled.removeBackHandler)
                            activity.onBackPressedDispatcher.addCallback(
                                lifecycleOwner,
                                this
                            )
                    }

                    EmaBackHandlerStrategy.Cancelled -> {

                    }
                }
            }
        }
    }

    fun remove() {
        backPressedCallback.remove()
        shouldRestore = true
    }

    fun restore() {
        if (shouldRestore) {
            activity.onBackPressedDispatcher.addCallback(lifecycleOwner,backPressedCallback)
            shouldRestore = false
        }
    }

    internal fun add() {
        activity.onBackPressedDispatcher.addCallback(lifecycleOwner,backPressedCallback)
    }
}