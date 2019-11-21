package com.lliujun.utils.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.lliujun.utils.R
import com.lliujun.utils.extensions.android.dpToPx
import com.lliujun.utils.extensions.android.setLeftCompoundDrawable
import com.lliujun.utils.extensions.android.setRightCompoundDrawable
import com.lliujun.utils.util.CompoundDrawableDetector

/**
 * 可以设置自动清除按钮, 以及可以切换密码显示状态的 EditText
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
open class MultiFunctionEditText(context: Context, attrs: AttributeSet) :
    AppCompatEditText(context, attrs) {

    private var mRightDrawable: Drawable? = null
        set(value) {
            field = value
            setRightCompoundDrawable(value)
        }

    private var mLeftDrawable: Drawable? = null
        set(value) {
            field = value
            setLeftCompoundDrawable(value)
        }

    private val showPasswordDrawable: Drawable
    private val hidePasswordDrawable: Drawable
    private val deleteDrawable: Drawable

    var isShowPasswordIcon = false
        set(value) {
            field = value
            setLeftCompoundDrawable(showPasswordDrawable)
        }
    var isShowDeleteIcon = false

    init {

        val typeArray = resources.obtainAttributes(attrs, R.styleable.lj_DeleteEditText)

        isShowPasswordIcon =
            typeArray.getBoolean(R.styleable.lj_DeleteEditText_lj_isShowPasswordDrawable, false)

        isShowDeleteIcon =
            typeArray.getBoolean(R.styleable.lj_DeleteEditText_lj_isShowDeleteDrawable, false)

        deleteDrawable = typeArray.getDrawable(R.styleable.lj_DeleteEditText_lj_deleteDrawable)
            ?: ContextCompat.getDrawable(context, R.drawable.lj_delete_small)!!

        showPasswordDrawable =
            typeArray.getDrawable(R.styleable.lj_DeleteEditText_lj_showPasswordDrawable)
                ?: ContextCompat.getDrawable(context, R.drawable.lj_show_password)!!

        hidePasswordDrawable =
            typeArray.getDrawable(R.styleable.lj_DeleteEditText_lj_hidePasswordDrawable)
                ?: ContextCompat.getDrawable(context, R.drawable.lj_hidden_password)!!

        typeArray.recycle()
        compoundDrawablePadding = 10.dpToPx().toInt()

        if (isShowPasswordIcon)
            mLeftDrawable = showPasswordDrawable
    }

    private val compoundDrawableDetector by lazy {
        CompoundDrawableDetector(this, object : CompoundDrawableDetector.OnClickListener {
            override fun onLeftClick() {
                if (isShowPasswordIcon) {
                    mLeftDrawable =
                        if (inputType != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                            inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                            setSelection(length())
                            hidePasswordDrawable
                        } else {
                            inputType =
                                InputType.TYPE_TEXT_VARIATION_PASSWORD.or(InputType.TYPE_CLASS_TEXT)
                            typeface = Typeface.DEFAULT
                            setSelection(length())
                            showPasswordDrawable
                        }
                }
            }
            override fun onRightClick() {
                setText("")
            }
        })
    }

    override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (isShowDeleteIcon) {
            setClearIconVisible(hasFocus() && text.isNotEmpty())
        }
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (isShowDeleteIcon) {
            setClearIconVisible(focused && length() > 0)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isShowDeleteIcon && !isShowPasswordIcon) {
            return super.onTouchEvent(event)
        }
        return if (compoundDrawableDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }

    private fun setClearIconVisible(visible: Boolean) {
        mRightDrawable = if (visible) deleteDrawable else null
    }
}
