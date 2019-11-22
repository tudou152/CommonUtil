package com.lliujun.utils.extensions.android

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(message: String) {
    context?.toast(message)
}

fun Fragment.toast(message: String, xOffset: Int, yOffset: Int) {
    context?.toast(message, xOffset, yOffset)
}

fun Fragment.toastBy(message: String, dx: Int, dy: Int) {
    context?.toast(message, dx, dy)
}
fun View.toast(message: String) {
    context.toast(message)
}

fun View.toast(message: String, xOffset: Int, yOffset: Int) {
    context.toast(message, xOffset, yOffset)
}

fun View.toastBy(message: String, dx: Int, dy: Int) {
    context.toast(message, dx, dy)
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/**
 * 重新设置 toast 的位置, 都为 0 的时候默认
 * */
fun Context.toast(message: String, xOffset: Int, yOffset: Int) {
    Toast.makeText(this.applicationContext, message, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.BOTTOM, xOffset, yOffset)
        show()
    }
}

/**
 * 在原始 toast 位置的基础之上,进行位置的调整
 * */
fun Context.toastBy(message: String, dx: Int = 0, dy: Int = 0) {
    Toast.makeText(this.applicationContext, message, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.BOTTOM, xOffset + dx, yOffset + dy)
        show()
    }
}
