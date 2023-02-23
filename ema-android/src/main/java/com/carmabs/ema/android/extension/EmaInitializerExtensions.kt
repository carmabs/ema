package com.carmabs.ema.android.extension

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.carmabs.ema.core.initializer.EmaInitializer

/**
 * Set a initializer for current bundle
 */
fun Bundle.setInitializer(
    initializer: EmaInitializer
): Bundle {
    putSerializable(EmaInitializer.KEY, initializer)
    return this
}

fun EmaInitializer?.toBundle():Bundle?{
    return this?.let { Bundle().setInitializer(it) }
}

/**
 * Get a initializer for current bundle
 */
fun Bundle.getInitializer(): EmaInitializer? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(EmaInitializer.KEY,EmaInitializer::class.java)
    } else {
        getSerializable(EmaInitializer.KEY) as? EmaInitializer
    }
}

/**
 * Get the incoming initializer from another activity by the [initializer] provided
 */
fun Activity.getInitializer(): EmaInitializer? {
    return intent?.let {
        it.extras?.getInitializer()

    }
}

/**
 * Set the initializer for the current activity intent
 */
fun Activity.setInitializer(initializer: EmaInitializer) {
    intent = Intent().apply { putExtra(EmaInitializer.KEY, initializer) }
}

/**
 * Set the initializer for the current fragment
 */
fun Fragment.setInitializer(initializer: EmaInitializer) {
    arguments = Bundle().setInitializer(initializer)
}

/**
 * Get the incoming initializer from another fragment/activity
 */
fun Fragment.getInitializer(): EmaInitializer? {
    return arguments?.let {
        if (it.containsKey(EmaInitializer.KEY)) {
            it.getSerializableCompat(EmaInitializer.KEY)
        } else
            null
    }
}
