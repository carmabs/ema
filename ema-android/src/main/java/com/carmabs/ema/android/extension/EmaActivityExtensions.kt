package com.carmabs.ema.android.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.core.os.BuildCompat
import androidx.fragment.app.Fragment


/**
 * Extensions for fragment
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

/**
 * Add a listener to handle the hardware back button.
 * @param listener Listener to handle the back event. It must return true to enable default back behaviour. False to enable custom
 * back behaviour.
 */
@SuppressLint("UnsafeOptInUsageError")
fun ComponentActivity.addOnBackPressedListener(listener: () -> Boolean) {
    if (BuildCompat.isAtLeastT()) {
        val onBackInvokedCallback = object : OnBackInvokedCallback {
            override fun onBackInvoked() {
                val backDefaultLaunched = listener.invoke()
                if (backDefaultLaunched) {
                    onBackInvokedDispatcher.unregisterOnBackInvokedCallback(this)
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        onBackInvokedDispatcher
            .registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT, onBackInvokedCallback
            )

    } else {
        onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val backDefaultLaunched = listener.invoke()
                    isEnabled = !backDefaultLaunched
                    if (backDefaultLaunched) {
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        )
    }

}

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("This class is not contained in an activity")
}
