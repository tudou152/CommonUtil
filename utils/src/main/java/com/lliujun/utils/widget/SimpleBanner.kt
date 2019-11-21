package com.lliujun.utils.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.lliujun.utils.util.LinearLayoutManagerWithSpeed

@Suppress("unused")
class SimpleBanner(context: Context, attributeSet: AttributeSet) :
        RecyclerView(context, attributeSet) {

    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private var currentPage = INIT_REAL_PAGE
    private var mCurrentTouchState = TOUCH_STATE_NONE
    var onItemClickListener: OnItemClickListener? = null

    private var gestureDetector = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            val x = e?.getX(0) ?: return super.onSingleTapUp(e)
            val y = e.getY(0)
            val view = findChildViewUnder(x, y) ?: return super.onSingleTapUp(e)
            val pos = mLinearLayoutManager.getPosition(view)
            return onItemClickListener?.onItemClick(pos) ?: super.onSingleTapUp(e)
        }
    })

    @Suppress("MemberVisibilityCanBePrivate")
    var isInfiniteScroll = true

    @Suppress("MemberVisibilityCanBePrivate")
    var isAutoChangePage = false
        set(value) {
            if (!field && value) {
                sendAutoScrollMessage()
            }
            field = value
        }

    var onPageChangeListener: OnPageChangeListener? = null

    private val mHandler: Handler = Handler(Looper.getMainLooper()) {
        if (it.what == UPDATE_PAGE_MESSAGE) {
            if (isAutoChangePage) {
                smoothScrollToPosition(currentPage + 1)
                sendAutoScrollMessage()
            }
        }
        false
    }

    init {
        setup(context)
    }

    private fun sendAutoScrollMessage() {
        mHandler.removeMessages(UPDATE_PAGE_MESSAGE)
        val message = mHandler.obtainMessage(UPDATE_PAGE_MESSAGE)
        mHandler.sendMessageDelayed(message, AUTO_SCROLL_INTERVAL)
    }

    private fun setup(context: Context) {

        mLinearLayoutManager = LinearLayoutManagerWithSpeed(context, HORIZONTAL, false)
        layoutManager = mLinearLayoutManager

        val pageSnapHelper = PagerSnapHelper()
        pageSnapHelper.attachToRecyclerView(this)

        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_IDLE) {

                    val pos = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition()
                    if (pos != NO_POSITION && currentPage != pos) {
                        onPageChangeListener?.onPageChanged(currentPage, pos)
                        onPageChangeListener?.onIndicatorChanged(changeIndexToProper(currentPage), changeIndexToProper(pos))
                        currentPage = pos

                        if (currentPage == mLinearLayoutManager.itemCount - 1) {
                            autoSwitchPage { _, to ->
                                currentPage = to
                            }
                        }
                    }
                }
            }
        })
        scrollToPosition(INIT_REAL_PAGE)

        sendAutoScrollMessage()
    }

    private fun changeIndexToProper(pos: Int): Int {
        return when (pos) {
            0 -> mLinearLayoutManager.itemCount - 3
            mLinearLayoutManager.itemCount - 1 -> 0
            else -> pos - 1
        }
    }

    /**
     * 无线轮播的逻辑实现
     * */
    private fun autoSwitchPage(action: ((Int, Int) -> Unit)? = null) {

        val firstCompletelyVisibleItemPosition = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition()
        val lastCompletelyVisibleItemPosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition()

        val firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition()
        val lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition()

        val startOrTopIndex = 0
        if (firstCompletelyVisibleItemPosition == startOrTopIndex && lastCompletelyVisibleItemPosition == startOrTopIndex && firstVisibleItem == startOrTopIndex && lastVisibleItem == startOrTopIndex) {
            val end = mLinearLayoutManager.itemCount - 2
            scrollToPosition(end)
            action?.invoke(startOrTopIndex, end)
        }

        val endOrBottomIndex = mLinearLayoutManager.itemCount - 1

        if (firstCompletelyVisibleItemPosition == endOrBottomIndex && lastCompletelyVisibleItemPosition == endOrBottomIndex && firstVisibleItem == endOrBottomIndex && lastVisibleItem == endOrBottomIndex) {
            val end = 1
            scrollToPosition(end)
            action?.invoke(endOrBottomIndex, end)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(e)

        mHandler.removeMessages(UPDATE_PAGE_MESSAGE)

        if (isInfiniteScroll) {
            when (e?.action ?: return super.onTouchEvent(e)) {
                // 用户手指触摸的一瞬间,切换一次
                MotionEvent.ACTION_DOWN -> {
                    mCurrentTouchState = TOUCH_STATE_DOWN
                    autoSwitchPage()
                }
                MotionEvent.ACTION_UP -> {
                    mCurrentTouchState = TOUCH_STATE_UP
                    mCurrentTouchState = TOUCH_STATE_NONE
                    if (isAutoChangePage)
                        sendAutoScrollMessage()
                }
                else -> {
                }
            }
        }

        return super.onTouchEvent(e)
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return true
    }

    interface OnPageChangeListener {
        fun onPageChanged(from: Int, to: Int)
        fun onIndicatorChanged(from: Int, to: Int)
    }

    interface OnItemClickListener {
        fun onItemClick(pos: Int): Boolean
    }

    @Suppress("unused")
    companion object {

        private const val UPDATE_PAGE_MESSAGE = 100000
        private const val AUTO_SCROLL_INTERVAL = 2000L

        private const val TOUCH_STATE_NONE = 0
        private const val TOUCH_STATE_DOWN = 1
        private const val TOUCH_STATE_UP = 2
        private const val TOUCH_STATE_MOVE = 3

        private const val INIT_REAL_PAGE = 1
    }

}