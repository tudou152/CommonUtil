package com.lliujun.utils.extensions.android

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment


fun Context.getDimension(@DimenRes resId: Int): Float {
    var dimen = 0f
    try {
        dimen = resources.getDimension(resId)
    } catch (e: Resources.NotFoundException) {
        e.printStackTrace()
    }
    return dimen
}

fun View.getDimension(@DimenRes resId: Int): Float {
    return context.getDimension(resId)
}

fun Fragment.getDimension(@DimenRes resId: Int): Float  {
    return context?.getDimension(resId) ?: 0f
}
