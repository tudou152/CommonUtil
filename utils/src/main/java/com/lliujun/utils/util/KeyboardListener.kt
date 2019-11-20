package com.lliujun.utils.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.graphics.Point
import android.graphics.Rect
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES.JELLY_BEAN
import android.os.Build.VERSION_CODES.JELLY_BEAN_MR1
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent


interface KeyboardListener {
    /**
     * call back
     * @param isShow         true is show else hidden
     * @param keyboardHeight keyboard height
     */
    fun onKeyboardChange(isShow: Boolean, keyboardHeight: Int)
}

/**
 * 监听键盘的弹出和隐藏以及切换事件, 并提供键盘的高度
 */
class KeyboardChangeListener private constructor(contextObj: Any, private val keyboardListener: KeyboardListener) :
        ViewTreeObserver.OnGlobalLayoutListener, LifecycleObserver {

    private var mShowFlag = false
    private var mWindow: Window? = null
    private var mContentView: View? = null
    private var lifeCycle: Lifecycle? = null
    private var previousKeyboardHeight: Int = 0

    private val screenHeight: Int
        @SuppressLint("ObsoleteSdkInt")
        get() {
            val defaultDisplay = mWindow!!.windowManager.defaultDisplay
            val screenHeight: Int
            val point = Point()
            if (VERSION.SDK_INT >= JELLY_BEAN_MR1) {
                defaultDisplay.getRealSize(point)
            } else {
                defaultDisplay.getSize(point)
            }
            screenHeight = point.y
            return screenHeight
        }

    init {
        if (contextObj is Activity) {
            mContentView = findContentView((contextObj as Activity?)!!)
            mWindow = contextObj.window

            if (contextObj is FragmentActivity) {
                lifeCycle = contextObj.lifecycle.apply {
                    addObserver(this@KeyboardChangeListener)
                }
            }
        } else if (contextObj is Dialog) {
            mContentView = findContentView((contextObj as Dialog?)!!)
            mWindow = contextObj.window

            if (contextObj is DialogFragment) {
                lifeCycle = contextObj.lifecycle.apply {
                    addObserver(this@KeyboardChangeListener)
                }
            }
        }

        if (lifeCycle == null) {
            addGlobalLayoutListenerWhenNessary()
        }
    }

    private fun findContentView(contextObj: Activity): View {
        return contextObj.findViewById<View>(android.R.id.content)
    }

    private fun findContentView(contextObj: Dialog): View {
        return contextObj.findViewById(android.R.id.content)
    }

    private fun addGlobalLayoutListenerWhenNessary() {
        if (mContentView != null && mWindow != null) {
            mContentView?.viewTreeObserver?.addOnGlobalLayoutListener(this)
        }
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_RESUME)
    @CallSuper
    fun onLifeCycleResume() {
        Log.e(TAG, "onLifeCycleResume add listener")
        addGlobalLayoutListenerWhenNessary()
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_PAUSE)
    @CallSuper
    fun onLifeCyclePause() {
        Log.e(TAG, "onLifeCyclePause remove listener")
        destroy()
    }

    override fun onGlobalLayout() {

        if (mContentView == null || mWindow == null) {
            return
        }

        val currentViewHeight = mContentView!!.height
        if (currentViewHeight == 0) {
            return
        }
        val screenHeight = screenHeight
        val windowBottom: Int
        val keyboardHeight: Int

        val rect = Rect()
        mWindow!!.decorView.getWindowVisibleDisplayFrame(rect)
        windowBottom = rect.bottom

        keyboardHeight = screenHeight - windowBottom

//        Log.d(TAG, "onGlobalLayout() called  screenHeight $screenHeight VisibleDisplayHeight $windowBottom, keyboardHeight: $keyboardHeight")
        val currentShow = keyboardHeight > MIN_KEYBOARD_HEIGHT
//        Log.d(TAG, "mShowFlag: $mShowFlag, currentShow: $currentShow")
        if (mShowFlag != currentShow || (mShowFlag && (previousKeyboardHeight != keyboardHeight))) {
            mShowFlag = currentShow
            keyboardListener.onKeyboardChange(currentShow, keyboardHeight)
        }
    }


    @SuppressLint("ObsoleteSdkInt")
    @Suppress("MemberVisibilityCanBePrivate")
    fun destroy() {
        if (mContentView != null) {
            if (VERSION.SDK_INT >= JELLY_BEAN) {
                mContentView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
            }
        }
    }


    @Suppress("unused")
    companion object {
        private const val TAG = "KeyboardChangeListener"
        private const val MIN_KEYBOARD_HEIGHT = 300

        fun create(activity: Activity, listener: KeyboardListener): KeyboardChangeListener {
            return KeyboardChangeListener(activity, listener)
        }

        /**
         * 该方法会使用 lifecycle 来管理自动管理注册和取消监听全局布局的事件
         * */
        fun create(activity: FragmentActivity, listener: KeyboardListener): KeyboardChangeListener {
            return KeyboardChangeListener(activity, listener)
        }

        fun create(dialog: Dialog, listener: KeyboardListener): KeyboardChangeListener {
            return KeyboardChangeListener(dialog, listener)
        }

        fun create(dialog: DialogFragment, listener: KeyboardListener): KeyboardChangeListener {
            return KeyboardChangeListener(dialog, listener)
        }
    }
}