package com.lliujun.utils.extensions.android

import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.widget.TextView
import androidx.core.graphics.contains

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

/**
 * 用户输入的文字绘制区域,包含光标
 * */
fun TextView.contentRect(): Rect {
    return Rect(
        compoundPaddingLeft,
        compoundPaddingTop,
        width - compoundPaddingRight,
        height - compoundPaddingBottom
    )
}

fun TextView.isTouchLeftCompoundDrawable(event: MotionEvent): Boolean {
    val drawable = compoundDrawables[0] ?: return false
    val contentRect = contentRect()

    val rect = RectF(
        (contentRect.left - drawable.intrinsicWidth - compoundDrawablePadding).toFloat(),
        (contentRect.top).toFloat(),
        (contentRect.left - compoundDrawablePadding).toFloat(),
        (contentRect.bottom).toFloat()
    )
    return rect.contains(event.x, event.y)
}

fun TextView.isTouchTopCompoundDrawable(event: MotionEvent): Boolean {
    val drawable = compoundDrawables[1] ?: return false
    val contentRect = contentRect()
    val centerX = (contentRect.left + contentRect.right) / 2f

    val rect = RectF(
        centerX - drawable.intrinsicWidth / 2f,
        (contentRect.top - drawable.intrinsicHeight - compoundDrawablePadding).toFloat(),
        centerX + drawable.intrinsicWidth / 2f,
        (contentRect.top - compoundDrawablePadding).toFloat()
    )

    return rect.contains(event.x, event.y)
}

fun TextView.isTouchRightCompoundDrawable(event: MotionEvent): Boolean {
    val drawable = compoundDrawables[2] ?: return false
    val contentRect = contentRect()
    val rect = RectF(
        (contentRect.right + compoundDrawablePadding).toFloat(),
        (contentRect.top).toFloat(),
        (contentRect.right + drawable.intrinsicWidth + compoundDrawablePadding).toFloat(),
        (contentRect.bottom).toFloat()
    )
    return rect.contains(event.x, event.y)
}

fun TextView.isTouchBottomCompoundDrawable(event: MotionEvent): Boolean {
    val drawable = compoundDrawables[3] ?: return false
    val contentRect = contentRect()
    val centerX = (contentRect.left + contentRect.right) / 2f
    val rect = RectF(
        centerX - drawable.intrinsicWidth / 2f,
        (contentRect.bottom + compoundDrawablePadding).toFloat(),
        centerX + drawable.intrinsicWidth / 2f,
        (contentRect.bottom + drawable.intrinsicHeight +  compoundDrawablePadding).toFloat()
    )
    return rect.contains(event.x, event.y)
}
