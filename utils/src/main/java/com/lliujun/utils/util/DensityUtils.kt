@file:Suppress("unused")

package com.lliujun.utils.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment

fun Context.windowManager(): WindowManager {
    return getSystemService(Context.WINDOW_SERVICE) as WindowManager
}

fun Context.getScreenSize(): Point {
    return Point().apply {
        windowManager().defaultDisplay.getSize(this)
    }
}

fun Context.getScreenWidth(): Int = getScreenSize().x
fun Context.getScreenHeight(): Int = getScreenSize().y

fun Fragment.getScreenWidth() = context?.getScreenWidth() ?: 0
fun Fragment.getScreenHeight() = context?.getScreenHeight() ?: 0

fun View.getScreenWidth() = context.getScreenWidth()
fun View.getScreenHeight() = context.getScreenHeight()

/**
 * 获取导航栏的尺寸
 */
fun Context.getActionBarSize(): Int {
    val styledAttributes =
            theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
    val actionBarSize = styledAttributes.getDimension(0, 0f).toInt()
    styledAttributes.recycle()
    return actionBarSize
}

fun Fragment.getActionBarSize(): Int {
    return context?.getActionBarSize() ?: 0
}

fun View.getActionBarSize(): Int {
    return context.getActionBarSize()
}

fun Float.pxToDp(): Float {
    val density = Resources.getSystem().displayMetrics.density
    return this / density + 0.5f
}

fun Int.pxToDp(): Float {
    val density = Resources.getSystem().displayMetrics.density
    return this / density + 0.5f
}

fun Int.dpToPx(): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics)
}

fun Float.dpToPx(): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)
}

fun Int.spToPx(): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), Resources.getSystem().displayMetrics)
}

fun Float.spToPx(): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics)
}

fun getZForCamera(z: Int): Float {
    return Resources.getSystem().displayMetrics.density  * z * -1
}
