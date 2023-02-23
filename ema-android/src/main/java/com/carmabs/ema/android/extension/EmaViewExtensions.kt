package com.carmabs.ema.android.extension

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.OvershootInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.carmabs.ema.android.ANIMATION_DURATION
import com.carmabs.ema.android.ANIMATION_OVERSHOOT
import com.carmabs.ema.core.constants.FLOAT_ZERO
import com.carmabs.ema.core.constants.INT_ZERO
import com.carmabs.ema.core.constants.STRING_EMPTY
import com.carmabs.ema.core.extension.emaRegexGetCharacterValue
import com.carmabs.ema.core.extension.emaRegexGetFloatValue
import java.text.NumberFormat
import java.util.*


/**
 * Extensions for views
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

/**
 * Listener to make view tasks after it has been measured
 */
inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    if (measuredHeight > 0 && measuredWidth > 0) {
        f()
    } else {
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (measuredWidth > 0 && measuredHeight > 0) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    f()
                }
            }
        })
    }
}

fun TextView.setIncrementAnimated(
    endValue: Int,
    formatter: String?,
    duration: Long = ANIMATION_DURATION
) {
    val integerString = text.toString().replace(emaRegexGetCharacterValue, STRING_EMPTY)
    val currentValue = integerString.toIntOrNull() ?: INT_ZERO
    val animator = ValueAnimator.ofInt(currentValue, endValue)
    animator.duration = duration
    animator.addUpdateListener {
        val value = it.animatedValue as Int
        text = formatter?.let { form -> String.format(form, value) } ?: value.toString()
    }
    animator.start()
}

fun TextView.setIncrementAnimated(
    endValue: Float,
    formatter: String?,
    duration: Long = ANIMATION_DURATION
) {
    val floatString = emaRegexGetFloatValue.find(text)?.value
    val format = NumberFormat.getInstance(Locale.getDefault());
    val currentValue =
        floatString?.let { (format.parse(it) as? Double)?.toFloat() ?: FLOAT_ZERO } ?: FLOAT_ZERO
    val animator = ValueAnimator.ofFloat(currentValue, endValue)
    animator.duration = duration
    animator.addUpdateListener {
        val value = it.animatedValue as Float
        text = formatter?.let { form -> String.format(form, value) } ?: value.toString()
    }
    animator.start()
}

fun EditText.setTextWithCursorAtEnd(text:String){
    setText(text)
    setSelection((text.length).coerceAtLeast(INT_ZERO))
}

fun Activity.hideKeyboard() {
    (findViewById<View>(android.R.id.content)).rootView.hideKeyboard()
}

fun Fragment.hideKeyboard() {
    requireView().rootView.hideKeyboard()
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
    clearFocus()
}


fun ImageView.setImageDrawableWithTransition(
    drawable: Drawable,
    durationMillis: Int = ANIMATION_DURATION.toInt()
) {
    getDrawable()?.also {
        val crossFadeTransition = TransitionDrawable(arrayOf(it, drawable))
        crossFadeTransition.isCrossFadeEnabled = true
        setImageDrawable(crossFadeTransition)
        crossFadeTransition.startTransition(durationMillis)
    }

}


fun ImageView.setImageWithOvershot(
    drawable: Drawable,
    overshoot: Float = ANIMATION_OVERSHOOT,
    durationMillis: Int = ANIMATION_DURATION.toInt(),
    rightDirection: Boolean = true,
    endAnimationListener: (() -> Unit)? = null
) {
    rotation = if (rightDirection) -overshoot else overshoot
    setImageDrawable(drawable)
    val animation = animate()
    animation.rotation(FLOAT_ZERO)
    animation.interpolator = OvershootInterpolator(overshoot)
    animation.duration = durationMillis.toLong()
    animation.setListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {

        }

        override fun onAnimationEnd(animation: Animator) {
            endAnimationListener?.invoke()
        }

        override fun onAnimationCancel(animation: Animator) {

        }

        override fun onAnimationRepeat(animation: Animator) {

        }

    })
    animation.start()
}


/**
 * Returns View visibility based on boolean
 * @param visibility True is VISIBLE, false INVISIBLE
 * @param gone Put this value true if visibility is false and you want to return GONE
 *
 */
fun checkVisibility(visibility: Boolean, gone: Boolean = true): Int {
    return when {
        visibility -> View.VISIBLE
        gone -> View.GONE
        else -> View.INVISIBLE
    }
}

/**
 * Executes an action if values are different
 *
 * @param oldValue Previous value to check update
 * @param newValue Current value to check update
 * @param action Function to execute with new value if it has changed
 */
fun <T> checkUpdate(oldValue: T, newValue: T, action: (T) -> Unit) {
    if (oldValue != newValue)
        action(newValue)
}

/**
 * Tint the progress bar with the selected color
 *
 * @param color The color tint
 */
fun ProgressBar.setProgressTintCompat(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        val wrapDrawable = DrawableCompat.wrap(progressDrawable)
        DrawableCompat.setTint(wrapDrawable, color)
        progressDrawable = DrawableCompat.unwrap(wrapDrawable)
    } else {
        progressTintList = ColorStateList.valueOf(color)
    }
}

/**
 * Tint the progress bar with the selected color
 *
 * @param color The color tint
 */
fun ProgressBar.setProgressGradientTintCompat(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        val wrapDrawable = DrawableCompat.wrap(progressDrawable) as GradientDrawable
        DrawableCompat.setTint(wrapDrawable, color)
        progressDrawable = DrawableCompat.unwrap(wrapDrawable)
    } else {
        progressTintList = ColorStateList.valueOf(color)
    }
}

fun getColorFromGradient(
    colors: IntArray,
    positions: FloatArray,
    v: Float
): Int {
    require(!(colors.size == 0 || colors.size != positions.size))
    if (colors.size == 1) {
        return colors[0]
    }
    if (v <= positions[0]) {
        return colors[0]
    }
    if (v >= positions[positions.size - 1]) {
        return colors[positions.size - 1]
    }
    for (i in 1 until positions.size) {
        if (v <= positions[i]) {
            val t =
                (v - positions[i - 1]) / (positions[i] - positions[i - 1])
            return lerpColor(colors[i - 1], colors[i], t)
        }
    }
    throw RuntimeException()
}

fun lerpColor(colorA: Int, colorB: Int, t: Float): Int {
    val alpha =
        Math.floor((Color.alpha(colorA) * (1 - t) + Color.alpha(colorB) * t).toDouble()).toInt()
    val red = Math.floor((Color.red(colorA) * (1 - t) + Color.red(colorB) * t).toDouble()).toInt()
    val green =
        Math.floor((Color.green(colorA) * (1 - t) + Color.green(colorB) * t).toDouble()).toInt()
    val blue =
        Math.floor((Color.blue(colorA) * (1 - t) + Color.blue(colorB) * t).toDouble()).toInt()
    return Color.argb(alpha, red, green, blue)
}