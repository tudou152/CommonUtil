@file:Suppress("unused")

package com.lliujun.utils.util.imageloader

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.lliujun.utils.util.imageloader.glide.GlideImageLoader


interface ImageLoader {

    val contextHolder: ContextHolder

    fun load(url: String, target: ImageView, config: Config? = null)

    data class Config(val round: Boolean = false, val placeHolder: Drawable? = null)

    class ContextHolder {
        var fragment: Fragment? = null
            private set
        var fragmentActivity: FragmentActivity? = null
            private set
        var activity: Activity? = null
            private set
        var context: Context? = null
            private set

        constructor(fragment: Fragment) {
            this.fragment = fragment
        }

        constructor(fragmentActivity: FragmentActivity) {
            this.fragmentActivity = fragmentActivity
        }

        constructor(activity: Activity) {
            this.activity = activity
        }

        constructor(context: Context) {
            this.context = context
        }
    }

    interface Factory {
        fun get(context: Context): ImageLoader
        fun get(fragment: Fragment): ImageLoader
        fun get(fragment: FragmentActivity): ImageLoader
        fun get(activity: Activity): ImageLoader
    }
}


object ImageLoaderFactory {

    private val factory = GlideImageLoader

    fun get(context: Context): ImageLoader {
        return factory.get(context)
    }

    fun get(fragment: Fragment): ImageLoader {
        return factory.get(fragment)

    }
    fun get(fragment: FragmentActivity): ImageLoader {
        return factory.get(fragment)
    }

    fun get(activity: Activity): ImageLoader {
        return factory.get(activity)
    }
}
