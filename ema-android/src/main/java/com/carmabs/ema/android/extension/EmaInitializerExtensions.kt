package com.carmabs.ema.android.extension

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.carmabs.ema.android.initializer.EmaInitializerBundle
import com.carmabs.ema.android.initializer.bundle.BundleSerializer
import com.carmabs.ema.android.initializer.bundle.strategy.BundleSerializerStrategy
import com.carmabs.ema.core.initializer.EmaInitializer


/**
 * Set a initializer for current bundle
 */
inline fun <reified I : EmaInitializer> Bundle.setInitializer(
    initializer: I,
    strategy: BundleSerializerStrategy
) {
    BundleSerializer(this, strategy).save(initializer)
}

fun EmaInitializer?.toBundle(serializerStrategy: BundleSerializerStrategy):Bundle?{
    return  this?.let {
        val bundle = Bundle()
        bundle.setInitializer(it,serializerStrategy)
        bundle
    }
}

fun EmaInitializerBundle?.toBundle():Bundle?{
    return  this?.let {
        val bundle = Bundle()
        bundle.setInitializer(it.initializer,it.serializer)
        bundle
    }
}

/**
 * Get a initializer for current bundle
 */
inline fun <reified I : EmaInitializer> Bundle.getInitializer(strategy: BundleSerializerStrategy): I? {
    return BundleSerializer(this, strategy).restore() as? I
}


/**
 * Get the incoming initializer from another activity by the initializer provided
 */
inline fun <reified I : EmaInitializer> Activity.getInitializer(strategy: BundleSerializerStrategy): I? {
    return intent?.let {
        it.extras?.let { bundle ->
            BundleSerializer(bundle, strategy).restore() as? I
        }
    }
}

/**
 * Set the initializer for the current activity intent
 */
inline fun <reified I : EmaInitializer> Activity.setInitializer(initializer: I,strategy: BundleSerializerStrategy) {
    intent = Intent().setInitializer(initializer,strategy)
}

/**
 * Set the initializer for the current activity intent
 */
inline fun <reified I : EmaInitializer> Intent.setInitializer(initializer: I,strategy: BundleSerializerStrategy):Intent {
    val bundle = Bundle()
    bundle.setInitializer(initializer,strategy)
    putExtras(bundle)
    return this
}

/**
 * Set the initializer for the current activity intent
 */
inline fun <reified I : EmaInitializer> Intent.getInitializer(strategy: BundleSerializerStrategy): I? {
    return extras?.getInitializer(strategy)
}

/**
 * Set the initializer for the current fragment
 */
inline fun <reified I : EmaInitializer> Fragment.setInitializer(
    initializer: I,
    strategy: BundleSerializerStrategy
) {
    val bundle = Bundle()
    bundle.setInitializer(initializer,strategy)
    arguments = bundle
}

/**
 * Get the incoming initializer from another fragment/activity
 */
inline fun <reified I : EmaInitializer> Fragment.getInitializer(serializerStrategy: BundleSerializerStrategy): I? {
    return arguments?.getInitializer(serializerStrategy)
}