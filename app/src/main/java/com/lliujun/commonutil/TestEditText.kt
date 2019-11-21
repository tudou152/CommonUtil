package com.lliujun.commonutil

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import com.lliujun.utils.extensions.android.dpToPx
import com.lliujun.utils.widget.MultiFunctionEditText

/**
 * 用于测试定位 compoundDrawable 的 TextView, 不要用这个类
 * */
@Suppress("unused")
class TestEditText(context: Context, attrs: AttributeSet) : MultiFunctionEditText(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        width = 2.dpToPx().toInt()
        color = Color.RED
    }

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)

        val outRect = Rect(
            paddingStart,
            paddingTop,
            width -  paddingEnd,
            height - paddingBottom
        )
        paint.color = Color.GREEN
        canvas?.drawRect(outRect, paint)

        val contentRect = Rect(
            compoundPaddingLeft,
            compoundPaddingTop,
             width - compoundPaddingRight,
             height - compoundPaddingBottom
        )

        compoundDrawables[0]?.let {drawable ->
            paint.color = Color.BLACK
            val rect = RectF(
                (contentRect.left - drawable.intrinsicWidth - compoundDrawablePadding).toFloat(),
                (contentRect.top).toFloat(),
                (contentRect.left - compoundDrawablePadding).toFloat(),
                (contentRect.bottom).toFloat()
            )
            Log.e("rect", "leftRect is $rect")
            canvas?.drawRect(rect, paint)
        }

        compoundDrawables[1]?.let {drawable ->

            val centerX = (contentRect.left + contentRect.right) / 2f
            paint.color = Color.BLACK
            val rect = RectF(
                centerX - drawable.intrinsicWidth / 2f,
                (contentRect.top - drawable.intrinsicHeight - compoundDrawablePadding).toFloat(),
                centerX + drawable.intrinsicWidth / 2f,
                (contentRect.top - compoundDrawablePadding).toFloat()
            )

            Log.e("rect", "topRect is $rect")
            canvas?.drawRect(rect, paint)
        }

        compoundDrawables[2]?.let {drawable ->
            paint.color = Color.BLACK
            val rect = RectF(
                (contentRect.right + compoundDrawablePadding).toFloat(),
                (contentRect.top).toFloat(),
                (contentRect.right + drawable.intrinsicWidth + compoundDrawablePadding).toFloat(),
                (contentRect.bottom).toFloat()
            )
            Log.e("rect", "rightRect is $rect")
            canvas?.drawRect(rect, paint)
        }

        compoundDrawables[3]?.let {drawable ->
            val centerX = (contentRect.left + contentRect.right) / 2f
            paint.color = Color.BLACK
            val rect = RectF(
                centerX - drawable.intrinsicWidth / 2f,
                (contentRect.bottom + compoundDrawablePadding).toFloat(),
                centerX + drawable.intrinsicWidth / 2f,
                (contentRect.bottom + drawable.intrinsicHeight +  compoundDrawablePadding).toFloat()
            )
            Log.e("rect", "bottomRect is $rect")
            canvas?.drawRect(rect, paint)
        }

        Log.e("onDraw", "compoundPadding: $contentRect")

        paint.color = Color.RED
        canvas?.drawRect(contentRect, paint)


        paint.color = Color.BLUE
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    companion object {
        private val TAG = TestEditText::class.java.simpleName
    }
}