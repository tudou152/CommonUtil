package com.lliujun.utils.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.lliujun.utils.R

/**
 * @author liu jun
 *
 * 圆圈指示器
 * */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class IndicatorView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    var strokeWidth = DEFAULT_STROKE_WIDTH
        set(value) {
            field = value
            paint.strokeWidth = value
            invalidate()
            requestLayout()
        }

    var radius = DEFAULT_RADIUS
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    var margin = DEFAULT_MARGIN
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    var normalColor = DEFAULT_NORMAL_COLOR
        set(value) {
            field = value
            invalidate()
        }

    var selectedColor = DEFAULT_SELECTED_COLOR
        set(value) {
            field = value
            invalidate()
        }

    var mUnselectedStyle : Int = MODE_FILL_AND_STROKE
        set(value) {
            field = value
            invalidate()
        }

    var itemCount: Int = DEFAULT_ITEM_COUNT
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    var selectedIndex: Int = DEFAULT_SELECTED_INDEX
        set(value) {
            field = value
            invalidate()
        }

    init {
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.lj_IndicatorView,
            0,0
        ).apply {

            itemCount = getInteger(R.styleable.lj_IndicatorView_lj_itemCount, DEFAULT_ITEM_COUNT)
            selectedIndex = getInteger(R.styleable.lj_IndicatorView_lj_selectedIndex, DEFAULT_SELECTED_INDEX)
            mUnselectedStyle = getInt(R.styleable.lj_IndicatorView_lj_unselectedStyle, MODE_FILL_AND_STROKE)
            margin = getDimension(R.styleable.lj_IndicatorView_lj_circleMargin, DEFAULT_MARGIN)

            selectedColor = getColor(R.styleable.lj_IndicatorView_lj_selectedColor, DEFAULT_SELECTED_COLOR)
            normalColor = getColor(R.styleable.lj_IndicatorView_lj_normalColor, DEFAULT_NORMAL_COLOR)


            radius = getDimension(R.styleable.lj_IndicatorView_lj_radius, DEFAULT_RADIUS)
            strokeWidth = getDimension(R.styleable.lj_IndicatorView_lj_strokeWidth, DEFAULT_STROKE_WIDTH)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = itemCount * (radius) * 2 + (itemCount - 1) * margin
        val height = (radius) * 2

        val widthMeasureSpecModify = MeasureSpec.makeMeasureSpec(width.toInt(), MeasureSpec.EXACTLY)
        val heightMeasureSpecModify = MeasureSpec.makeMeasureSpec(height.toInt(), MeasureSpec.EXACTLY)

        setMeasuredDimension(widthMeasureSpecModify, heightMeasureSpecModify)
    }



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for (index in 0 until itemCount) {

            paint.style = if (index == selectedIndex) {
                paint.color = selectedColor

                Paint.Style.FILL_AND_STROKE
            } else {
                paint.color = normalColor

                when(mUnselectedStyle) {
                    MODE_STROKE -> Paint.Style.STROKE
                    else -> Paint.Style.FILL_AND_STROKE
                }
            }

            val x = radius + index * (margin + 2 * (radius))
            val y = radius

            canvas?.drawCircle(x, y, radius - strokeWidth, paint)
        }
    }

    companion object {

        private const val DEFAULT_STROKE_WIDTH = 1f
        private const val DEFAULT_RADIUS = 12f
        private const val DEFAULT_MARGIN = 30f

        private val DEFAULT_NORMAL_COLOR = Color.parseColor("#D6E9FF")
        private val DEFAULT_SELECTED_COLOR = Color.parseColor("#4396F9")

        private const val DEFAULT_ITEM_COUNT = 0
        private const val DEFAULT_SELECTED_INDEX = 0

        /**
         * 选中或者未选中,都是填充的模式
         * */
        const val MODE_FILL_AND_STROKE = 0

        /**
         * 选中的时候是只描边并填充, 没有选中的时候只描边
         * */
        const val MODE_STROKE = 1
    }
}