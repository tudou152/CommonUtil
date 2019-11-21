package com.lliujun.utils.widget

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.StaticLayout
import android.util.AttributeSet
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.SpannableString
import android.text.Layout
import android.view.View
import androidx.appcompat.widget.AppCompatTextView

/**
 * 可以展开的textView
 * */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class UnFoldAbleTextView : AppCompatTextView, View.OnClickListener {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    private var ellipsizeString: SpannableString? = null

    private var unEllipsizeString: SpannableString? = null
    private var content: String? = null
    private val spanColor by lazy { ForegroundColorSpan(Color.parseColor("#0079e2")) }

    private var isFolded = false
    private var mMaxLines : Int = -1

    var isFoldAble = true

    @Suppress("DEPRECATION")
    private fun getStaticLayout(content: String, width: Int): StaticLayout {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return StaticLayout.Builder.obtain(content, 0, content.length, paint, width)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(0f, 1f)
                    .build()
        } else {
            StaticLayout(content, paint, width, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)
        }
    }

    override fun setMaxLines(maxLines: Int) {
        if (mMaxLines == -1) {
            mMaxLines = maxLines
        }
        super.setMaxLines(maxLines)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (!isFoldAble) return
        val width = MeasureSpec.getSize(widthMeasureSpec)
        if (content == null) {
            content = text.toString()
            getNewText(width)
        }
    }

    private fun getNewText(width: Int) {
        val staticLayout = getStaticLayout(content!!, width)
        if (staticLayout.lineCount > maxLines) { // 需要进行折叠操作
            val foldString = "收起"
            //给收起两个字设成蓝色
            unEllipsizeString = SpannableString("""$content  $foldString""")
            unEllipsizeString!!.setSpan(spanColor, unEllipsizeString!!.length - foldString.length, unEllipsizeString!!.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            //获取到第三行最后一个文字的下标
            val index = staticLayout.getLineStart(maxLines) - 1
            //定义收起后的文本内容
            val unfoldString = "查看全部"
            val substring = content.toString().substring(0, index - unfoldString.length) + "..." + unfoldString

            ellipsizeString = SpannableString(substring)
            //给查看全部设成蓝色
            ellipsizeString!!.setSpan(spanColor, substring.length - unfoldString.length, substring.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            //设置收起后的文本内容
            text = ellipsizeString
            setOnClickListener(this)
            isFolded = true
        }
    }

    override fun onClick(v: View?) {
        if (isFolded) { // 展开
            isFolded = false
            maxLines = Int.MAX_VALUE
            text = unEllipsizeString
        } else {
            isFolded = true
            maxLines = mMaxLines
            text = ellipsizeString
        }
    }

}