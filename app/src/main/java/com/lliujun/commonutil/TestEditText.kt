package com.lliujun.commonutil

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import com.lliujun.utils.extensions.android.dpToPx
import com.lliujun.utils.extensions.android.toast
import com.lliujun.utils.util.CompoundDrawableDetector

/**
 * 用于测试定位 compoundDrawable 的 TextView, 不要用这个类
 * */
@Suppress("unused")
class TestEditText(context: Context, attrs: AttributeSet) : EditText(context, attrs) {

    private val compoundDrawableDetector by lazy {
        CompoundDrawableDetector(this, object : CompoundDrawableDetector.OnClickListener {
            override fun onLeftClick() {
                toast("left click")
            }
            override fun onRightClick() {
                toast("right click")
            }
            override fun onTopClick() {
                toast("click top")
            }
            override fun onBottomClick() {
                toast("click bottom")
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (compoundDrawableDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }
}