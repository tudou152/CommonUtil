package com.lliujun.utils.util.imageloader

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

interface ImageLoader {
    /**
     * 加载图片
     * */
    fun load(context: Context, url: String, view: ImageView, config: Config? = null)
    fun load(activity: AppCompatActivity, url: String, view: ImageView, config: Config? = null)
    fun load(fragment: Fragment, url: String, view: ImageView, config: Config? = null)
    fun load(fragment: FragmentActivity, url: String, view: ImageView, config: Config? = null)

    class Config(
        val round: Boolean = false,
        val placeHolder: Drawable? = null
    )
}