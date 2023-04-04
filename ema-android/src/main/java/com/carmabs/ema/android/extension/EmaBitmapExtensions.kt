package com.carmabs.ema.android.extension

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Size
import androidx.annotation.ColorInt
import com.carmabs.ema.core.constants.FLOAT_ZERO
import com.carmabs.ema.core.constants.INT_ONE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt


/**
 * Created by Carlos Mateo Benito on 2020-07-13.
 *
 * <p>
 * Copyright (c) 2020 by atSistemas. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:cmateo.benito@atsistemas.com”>Carlos Mateo Benito</a>
 */
fun Bitmap.getRoundedCornerBitmap(
    pixels: Int = width / 2,
    paint: Paint = Paint(),
    rect: Rect = Rect(),
    rectF: RectF = RectF(),
    outputBitmap: Bitmap? = null
): Bitmap {
    val output: Bitmap = outputBitmap ?: Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val color = -0xbdbdbe
    val pColor = paint.color
    val pAlias = paint.isAntiAlias
    val pXferMode = paint.xfermode

    rect.set(0, 0, width, height)
    rectF.set(rect)
    val roundPx = pixels.toFloat()
    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    paint.color = color
    canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, rect, rect, paint)

    paint.xfermode = pXferMode
    paint.color = pColor
    paint.isAntiAlias = pAlias
    return output
}

fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, stream)
    return stream.toByteArray()
}

fun Bitmap.copyDefault(mutable:Boolean = true): Bitmap {
    return copy(config,mutable)
}

suspend fun ByteArray.toBitmap(width: Int? = null, height: Int? = null,@ColorInt colorTint:Int?=null): Bitmap {
    return withContext(Dispatchers.Default) {
        val resultBitmap = if (width != null && height != null) {
            Bitmap.createScaledBitmap(
                BitmapFactory.decodeByteArray(
                    this@toBitmap,
                    0,
                    size,
                    BitmapFactory.Options().apply {
                        outHeight = height
                        outWidth = width
                    }), width, height, true
            )
        } else {
            BitmapFactory.decodeByteArray(this@toBitmap, 0, size)
        }
        colorTint?.let {
            val tintBitmap = resultBitmap.copy(resultBitmap.config,true)
            val paint = Paint().apply {
                colorFilter = PorterDuffColorFilter(it,PorterDuff.Mode.SRC_IN)
            }
            Canvas(tintBitmap).drawBitmap(resultBitmap,0f,0f,paint)
            tintBitmap
        }?:resultBitmap
    }
}

fun Bitmap.resizeCrop(): Bitmap {
    val dstBmp = if (width >= height) {
        Bitmap.createBitmap(
            this,
            this.width / 2 - this.height / 2,
            0,
            this.height,
            this.height
        );

    } else {
        Bitmap.createBitmap(
            this,
            0,
            this.height / 2 - this.width / 2,
            this.width,
            this.width
        );
    }
    return dstBmp
}

fun Bitmap.resizeFitInside(destWidth: Int, destHeight: Int): Bitmap {
    val background = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.ARGB_8888)
    val originalWidth = this.width.toFloat()
    val originalHeight = this.height.toFloat()
    val canvas = Canvas(background)
    val scaleX = destWidth.toFloat() / originalWidth
    val scaleY = destHeight.toFloat() / originalHeight
    var xTranslation = FLOAT_ZERO
    var yTranslation = FLOAT_ZERO
    var scale = 1f
    if (scaleX < scaleY) { // Scale on X, translate on Y
        scale = scaleX
        yTranslation = (destHeight - originalHeight * scale) / 2.0f
    } else { // Scale on Y, translate on X
        scale = scaleY
        xTranslation = (destWidth - originalWidth * scale) / 2.0f
    }
    val transformation = Matrix()
    transformation.postTranslate(xTranslation, yTranslation)
    transformation.preScale(scale, scale)
    val paint = Paint()
    paint.isFilterBitmap = true
    canvas.drawBitmap(this, transformation, paint)
    return background
}

fun Drawable.toBitmapWithMaxSize(maxSize: Size? = null): Bitmap {
    return if (this is BitmapDrawable && this.bitmap != null) {
        val bitmapDrawable = this
        if (maxSize == null) {
            return bitmapDrawable.bitmap
        }

        val maxHeight = if (maxSize.height > intrinsicHeight)
            intrinsicHeight
        else
            maxSize.height

        val maxWidth = if (maxSize.width > intrinsicWidth)
            intrinsicWidth
        else
            maxSize.width

        val originBitmap = bitmap
        val originRatio = intrinsicWidth / intrinsicHeight.toFloat()
        val maxRatio = maxWidth / maxHeight.toFloat()

        val centerX = maxWidth / 2f
        val centerY = maxHeight / 2f

        val targetWidth = maxWidth
        val targetHeight = maxHeight
        val scaledBitmap = Bitmap.createBitmap(maxWidth, maxHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(scaledBitmap)
        val centerOriginX = originBitmap.width/2f
        val centerOriginY = originBitmap.height/2f
        var targetOriginWidth = originBitmap.width
        var targetOriginHeight = originBitmap.height

        if (originRatio > INT_ONE) {
            targetOriginWidth = (targetOriginHeight * maxRatio).roundToInt()
        } else {
            targetOriginHeight = (targetOriginWidth / maxRatio).roundToInt()
        }

        canvas.drawBitmap(
            originBitmap,
            Rect(
                (centerOriginX - targetOriginWidth / 2).roundToInt(),
                (centerOriginY - targetOriginHeight / 2).roundToInt(),
                (centerOriginX + targetOriginWidth / 2).roundToInt(),
                (centerOriginY + targetOriginHeight / 2).roundToInt()
            ),
            Rect(
                (centerX - targetWidth / 2).roundToInt(),
                (centerY - targetHeight / 2).roundToInt(),
                (centerX + targetWidth / 2).roundToInt(),
                (centerY + targetHeight / 2).roundToInt()
            ),
            Paint().apply { isAntiAlias = true }
        )
        scaledBitmap
    } else {
        Bitmap.createBitmap(
            1,
            1,
            Bitmap.Config.ARGB_8888
        ) // Single color bitmap will be created of 1x1 pixel
    }
}
