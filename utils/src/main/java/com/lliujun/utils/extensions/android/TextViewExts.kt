package com.lliujun.utils.extensions.android

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.widget.TextView

fun TextView.setLeftCompoundDrawable(drawable: Drawable?) {
    setCompoundDrawablesWithIntrinsicBounds(
            drawable,
            compoundDrawables[1],
            compoundDrawables[2],
            compoundDrawables[3])
}

fun TextView.setTopCompoundDrawable(drawable: Drawable?) {
    setCompoundDrawablesWithIntrinsicBounds(
            compoundDrawables[0],
            drawable,
            compoundDrawables[2],
            compoundDrawables[3])
}

fun TextView.setRightCompoundDrawable(drawable: Drawable?) {
    setCompoundDrawablesWithIntrinsicBounds(
            compoundDrawables[0],
            compoundDrawables[1],
            drawable,
            compoundDrawables[3])
}

fun TextView.setBottomCompoundDrawable(drawable: Drawable?) {
    setCompoundDrawablesWithIntrinsicBounds(
            compoundDrawables[0],
            compoundDrawables[1],
            compoundDrawables[2],
            drawable)
}

 fun TextView.isTouchLeftCompoundDrawable(event: MotionEvent): Boolean {
    val drawable = compoundDrawables[0] ?: return false
    val rect = Rect(
            paddingStart,
            drawable.bounds.top,
            paddingStart + drawable.bounds.width(),
            drawable.bounds.bottom
    )
    return rect.contains(event.x.toInt(), event.y.toInt())
}

 fun TextView.isTouchTopCompoundDrawable(event: MotionEvent): Boolean {
    val drawable = compoundDrawables[1] ?: return false
    val rect = Rect(
            drawable.bounds.left,
            paddingTop,
            drawable.bounds.right,
            paddingTop + drawable.bounds.height()
    )
    return rect.contains(event.x.toInt(), event.y.toInt())
}

 fun TextView.isTouchRightCompoundDrawable(event: MotionEvent): Boolean {
    val drawable = compoundDrawables[2] ?: return false
    val rect = Rect(
            width - drawable.bounds.width() - paddingEnd,
            drawable.bounds.top,
            width - paddingEnd,
            drawable.bounds.bottom
    )
    return rect.contains(event.x.toInt(), event.y.toInt())
}

 fun TextView.isTouchBottomCompoundDrawable(event: MotionEvent): Boolean {
    val drawable = compoundDrawables[3] ?: return false
    val rect = Rect(
            drawable.bounds.left,
            height - drawable.bounds.height() - paddingBottom,
            drawable.bounds.right,
            height - paddingBottom
    )
    return rect.contains(event.x.toInt(), event.y.toInt())
}
