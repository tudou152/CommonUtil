@file:Suppress("UNUSED_PARAMETER")

package com.lliujun.utils.extensions.android

import android.content.Context
import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import kotlin.reflect.KClass


fun View.getString(@StringRes resId: Int): String = resources.getString(resId)
fun View.getColor(@ColorRes resId: Int) = ContextCompat.getColor(context, resId)
fun View.getDrawable(@DrawableRes resId: Int) = ContextCompat.getDrawable(context, resId)

fun View.showKeyBoard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: Throwable) {

    }
    return false
}

fun View.afterMeasured(event: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth != 0 && measuredHeight != 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                event()
            }
        }
    })
}

fun View.setWidth(width: Int) {
    layoutParams.width = width
}

fun View.setHeight(height: Int) {
    layoutParams.height = height
}

/**
 * 每次只能响应一次点击事件,如果需要响应下次事件,则需要手动清除标记
 * */
fun View.onConfirmClickListener(onClickListener: (View?, ((Boolean) -> Unit)) -> Unit) {
    setOnClickListener(object : View.OnClickListener {
        var isClickable = true
        override fun onClick(v: View?) {
            if (isClickable) {
                isClickable = false
                onClickListener(v) { isClickable = it }
            }
        }
    })
}

fun View.animatedToX(x: Float, duration: Long = 250, endAction: (() -> Unit)? = null) {
    animate().x(x).setDuration(duration)
            .withEndAction(endAction)
            .start()
}

fun View.animatedToY(y: Float, duration: Long = 250, endAction: (() -> Unit)? = null) {
    animate().y(y).setDuration(duration)
            .withEndAction(endAction)
            .start()
}

fun View.animatedToTop(duration: Long = 250, endAction: (() -> Unit)? = null) {
    if (y == top.toFloat()) return
    animatedToY(top.toFloat())
}

fun View.animatedToBottom(duration: Long = 250, endAction: (() -> Unit)? = null) {
    if (y == bottom.toFloat()) return
    animatedToY(bottom.toFloat())
}

fun View.doOnLayout(block: ((View) -> Boolean)) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
                view: View, left: Int, top: Int, right: Int, bottom: Int,
                oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int
        ) {
            if (block(view)) {
                view.removeOnLayoutChangeListener(this)
            }
        }
    })
}

fun View.donGlobalLayout(block: () -> Unit) {
    viewTreeObserver.apply {
        addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                block()
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }
}

fun <T : View> View.containsView(clazz: KClass<T>): T? {
    if (this is ViewGroup) {
        if (this.childCount == 0) {
            return null
        } else {
            for (index in 0 until this.childCount) {
                return this.getChildAt(index).containsView(clazz)
            }
            return null
        }
    } else {
        return if (this::class == clazz) {
            this as T
        } else {
            null
        }

    }
}

fun <T : View> View.containsViewUnder(clazz: KClass<T>, x: Float, y: Float): T? {
    if (this is ViewGroup) {
        if (this.childCount == 0) return null
        if (!this.inRect(x, y)) return null

        var view: T? = null
        for (index in 0 until childCount) {
            view = this.getChildAt(index).containsViewUnder(clazz, x, y)
            if (view != null) {
                return view
            }
        }
        return view
    } else {
        return if (this.inRect(x, y) && clazz.java.isInstance(this)) {
            this as T
        } else {
            null
        }
    }
}

fun View.inRect(x: Float, y: Float): Boolean {
    return x >= left && x <= right && y >= top && y <= bottom
}

fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener { block(it as T) }

fun <T : View> T.longClick(block: (T) -> Boolean) = setOnLongClickListener { block(it as T) }


/**
 * 给一个 View 添加额外的点击区域
 * */
fun View.addTouchPadding(target: View, padding: Int) {
    post {
        val rect = Rect()
        target.getHitRect(rect)
        rect.left -= padding
        rect.top -= padding
        rect.right += padding
        rect.bottom += padding

        touchDelegate = TouchDelegate(rect, target)
    }
}

