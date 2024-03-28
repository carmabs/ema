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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStateAtLeast
import com.carmabs.ema.android.navigation.EmaNavigationBackHandler
import com.carmabs.ema.core.model.EmaBackHandlerStrategy
import kotlinx.coroutines.launch


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
fun ComponentActivity.addOnBackPressedListener(
    lifecycleOwner: LifecycleOwner? = null,
    listener: () -> EmaBackHandlerStrategy
): EmaNavigationBackHandler {
    //Added this due to bug https://issuetracker.google.com/issues/199631325
    //This guarantees that listener are restored in same order
    var added = false
    val backHandler = EmaNavigationBackHandler(
        activity = this,
        lifecycleOwner = lifecycleOwner ?: this,
        listener = listener
    )
    lifecycleOwner?.lifecycleScope?.launch {
        lifecycleOwner.withStateAtLeast(Lifecycle.State.CREATED){
            if(added) {
                backHandler.remove()
                added = false
            }
        }
    }
    lifecycleOwner?.lifecycleScope?.launch {
        lifecycleOwner.withStateAtLeast(Lifecycle.State.STARTED){
            if(!added) {
                backHandler.add()
                added = true
            }
        }
    }

    return backHandler

}

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("This class is not contained in an Activity")
}

fun Context.findComponentActivity(): ComponentActivity {
    var context = this
    while (context is ContextWrapper) {
        if (context is ComponentActivity) return context
        context = context.baseContext
    }
    throw IllegalStateException("This class is not contained in a ComponentActivity")
}
