package com.carmabs.ema.android.extension

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.carmabs.ema.core.constants.FLOAT_ZERO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


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
    outputBitmap:Bitmap?=null
): Bitmap {
    val output: Bitmap = outputBitmap?:Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
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

suspend fun ByteArray.toBitmap(width: Int? = null, height: Int? = null): Bitmap {
    return withContext(Dispatchers.Default) {
        if (width != null && height != null) {
            Bitmap.createScaledBitmap(
                BitmapFactory.decodeByteArray(
                    this@toBitmap,
                    0,
                    size,
                    BitmapFactory.Options().apply {
                        outHeight = height
                        outWidth = width
                    }), width, height,true)
        } else {
            BitmapFactory.decodeByteArray(this@toBitmap, 0, size)
        }
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

fun Drawable.toBitmap(width: Int?=null,height: Int?=null): Bitmap {
    if (this is BitmapDrawable) {
        val bitmapDrawable = this
        if (bitmapDrawable.bitmap != null && (width==null && height==null)) {
            return bitmapDrawable.bitmap
        }
    }

    val bitmap = if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
        Bitmap.createBitmap(
            1,
            1,
            Bitmap.Config.ARGB_8888
        ) // Single color bitmap will be created of 1x1 pixel
    } else {
        Bitmap.createBitmap(
            width?:intrinsicWidth,
            height?:intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
    }

    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}

