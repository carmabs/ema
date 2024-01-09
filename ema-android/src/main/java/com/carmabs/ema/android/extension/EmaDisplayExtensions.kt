package com.carmabs.ema.android.extension

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Insets
import android.os.Build
import android.util.DisplayMetrics
import android.util.Size
import android.util.TypedValue
import android.view.WindowInsets
import android.view.WindowManager
import com.carmabs.ema.core.constants.INT_ZERO
import kotlin.math.roundToInt


/**
 * Extension methods for display feature
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */


/**
 * Return the Int dp into its equivalent px
 */
val Int.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).roundToInt()

/**
 * Return the Int dp into its equivalent px
 */
val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )


/**
 * Return the Int sp into its equivalent px
 */
val Int.sp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).roundToInt()

/**
 * Return the Float sp into its equivalent px
 */
val Float.sp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        Resources.getSystem().displayMetrics
    )

/**
 * Get display metrics
 */
fun getScreenMetrics(context: Context,excludeInsets:Boolean = false): DisplayMetrics {
    val displayMetrics = context.resources.displayMetrics
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val metrics = windowManager.currentWindowMetrics
        // Gets all excluding insets
        val windowInsets = metrics.windowInsets
        val insets: Insets = windowInsets.getInsetsIgnoringVisibility(
            WindowInsets.Type.navigationBars()
                    or WindowInsets.Type.displayCutout()
        )

        val insetsWidth: Int = insets.right + insets.left
        val insetsHeight: Int = insets.top + insets.bottom


        // Legacy size that Display#getSize reports
        val bounds = metrics.bounds
        val legacySize = Size(
            bounds.width() - insetsWidth,
            bounds.height() - insetsHeight
        )
        displayMetrics.apply {
            heightPixels = legacySize.height
            widthPixels = legacySize.width
        }
        /**
         * Get the application size including decoration
         *
         *
        */
        if(excludeInsets) {
            val boundsActivity = windowManager.currentWindowMetrics.bounds
            displayMetrics.apply {
                heightPixels = boundsActivity.height()
                widthPixels = boundsActivity.width()
            }
        }

    } else {
        windowManager.defaultDisplay.getMetrics(displayMetrics)
    }
    return displayMetrics
}

fun Context.getStatusBarHeight(): Int {
    val resourceId: Int =
        resources.getIdentifier("status_bar_height", "dimen", "android")

    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else
        INT_ZERO
}

val Configuration.screenHeightPx
    get() = run { screenHeightDp.toFloat() * densityDpi / 160f }

val Configuration.screenWidthPx
    get() = run { screenWidthDp.toFloat() * densityDpi / 160f }