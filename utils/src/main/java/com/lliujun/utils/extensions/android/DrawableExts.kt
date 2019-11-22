package com.lliujun.utils.extensions.android

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

/**
 * 当drawableId 是一张矢量图形的时候,可以通过该方法获取一个drawable对象
 * @param context
 * @param drawableId
 * @return
 */
fun getBitmapFromDrawableRes(context: Context, @DrawableRes drawableId: Int): Bitmap? {
    val drawable: Drawable? = ContextCompat.getDrawable(context, drawableId)
    return drawable?.toBitMap()
}

fun Drawable.toBitMap(): Bitmap {
    var drawable = this
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        drawable = DrawableCompat.wrap(drawable).mutate()
    }

    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap!!)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}