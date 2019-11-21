package com.lliujun.utils.util

import android.view.MotionEvent
import android.widget.TextView
import com.lliujun.utils.extensions.android.isTouchBottomCompoundDrawable
import com.lliujun.utils.extensions.android.isTouchLeftCompoundDrawable
import com.lliujun.utils.extensions.android.isTouchRightCompoundDrawable
import com.lliujun.utils.extensions.android.isTouchTopCompoundDrawable
import kotlin.math.abs

/**
 * 可以探测在 TextView 中设置 compoundDrawable 之后,drawable 上的点击事件
 * 具体使用方法如下:
 * 1.创建该对象 val detector = CompoundDrawableDetector(textView, listener)
 * 2.在 onToutchEvent 方法中调用
 *
 * override fun onTouchEvent(event: MotionEvent): Boolean {
 *
 *     return if (compoundDrawableDetector.onTouchEvent(event)) {
 *         true
 *     } else {
 *         super.onTouchEvent(event)
 *     }
 * }
 * */
@Suppress("unused")
class CompoundDrawableDetector(
    private val textView: TextView,
    private val onClickListener: OnClickListener
) {

    private var detectedCompoundDrawableClickEvent = false

    init {
        textView.setOnFocusChangeListener { _, _ ->
            detectedCompoundDrawableClickEvent = false
        }
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            detectCompoundDrawableClickEvent(event)
        }
        return detectedCompoundDrawableClickEvent
    }

    private fun detectCompoundDrawableClickEvent(event: MotionEvent) {
        with(textView) {
            for ((index, drawable) in compoundDrawables.withIndex()) {
                if (drawable == null) continue
                when (index) {
                    DRAWABLE_LEFT ->
                        if (isTouchLeftCompoundDrawable(event)) {
                            detectedCompoundDrawableClickEvent = true
                            onClickListener.onLeftClick()
                        }
                    DRAWABLE_TOP ->
                        if (isTouchTopCompoundDrawable(event)) {
                            detectedCompoundDrawableClickEvent = true
                            onClickListener.onTopClick()
                        }
                    DRAWABLE_RIGHT ->
                        if (isTouchRightCompoundDrawable(event)) {
                            detectedCompoundDrawableClickEvent = true
                            onClickListener.onRightClick()
                        }
                    DRAWABLE_BOTTOM ->
                        if (isTouchBottomCompoundDrawable(event)) {
                            detectedCompoundDrawableClickEvent = true
                            onClickListener.onBottomClick()
                        }
                }
            }
        }
    }

    interface OnClickListener {
        fun onLeftClick() {}
        fun onRightClick() {}
        fun onTopClick() {}
        fun onBottomClick() {}
    }

    companion object {
        private const val DRAWABLE_LEFT = 0
        private const val DRAWABLE_TOP = 1
        private const val DRAWABLE_RIGHT: Int = 2
        private const val DRAWABLE_BOTTOM = 3
    }
}