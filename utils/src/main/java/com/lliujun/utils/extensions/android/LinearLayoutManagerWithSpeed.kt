package com.lliujun.utils.extensions.android

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

/**
 * 可以修改滑动速度的 RecyclerView
 * */
@Suppress("unused")
class LinearLayoutManagerWithSpeed(
    context: Context,
    @RecyclerView.Orientation orientation: Int,
    reverseLayout: Boolean,
    var speed: Float = 70f
) : LinearLayoutManager(context, orientation, reverseLayout) {

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView?,
        state: RecyclerView.State?,
        position: Int
    ) {
        val linearSmoothScroller = object: LinearSmoothScroller(recyclerView?.context) {
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                return speed / displayMetrics!!.densityDpi
            }
        }
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }

}