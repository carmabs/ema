package com.carmabs.ema.android.extension

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.graphics.drawable.toBitmap
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.carmabs.ema.core.value.EmaUriRes

/**
 * Created by Carlos Mateo Benito on 2/2/24.
 *
 * <p>
 * Copyright (c) 2024 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */

fun EmaUriRes.requireDrawable(context: Context): Drawable {
    return getDrawable(context) ?:getVectorDrawable(context)!!
}

@SuppressLint("DiscouragedApi")
fun EmaUriRes.getVectorDrawable(context: Context):Drawable? {
    val resourceId = context.resources.getIdentifier(Uri.parse(value).lastPathSegment, "drawable", context.packageName)
    return VectorDrawableCompat.create(context.resources, resourceId, context.theme)?.current
}

fun EmaUriRes.getDrawable(context: Context): Drawable? {
    val inputStream = context.contentResolver.openInputStream(Uri.parse(value))
    return Drawable.createFromStream(inputStream, value)
}

fun EmaUriRes.getBitmap(context: Context, width: Int? = null, height: Int? = null): Bitmap {
    val drawable = requireDrawable(context)
    val dimensions = drawable.getProportionalDimensions(width,height)
    return drawable.toBitmap(dimensions.first, dimensions.second)
}