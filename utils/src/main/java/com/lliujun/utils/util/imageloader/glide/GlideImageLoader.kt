package com.lliujun.utils.util.imageloader.glide

import android.app.Activity
import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.lliujun.utils.util.imageloader.ImageLoader
import java.lang.Exception

class GlideImageLoader(override val contextHolder: ImageLoader.ContextHolder) : ImageLoader {

    private fun setup(url: String, config: ImageLoader.Config?, requestManager: RequestManager, view: ImageView) {
        var builder = if (url.endsWith(".svg")) {
            // 处理svg图片
            requestManager
                    .`as`(PictureDrawable::class.java)
                    .addListener(SvgSoftwareLayerSetter())
                    .load(url)
        } else {
            requestManager.load(url)
        }

        if (config?.round == true) {
            builder = builder.apply(RequestOptions.bitmapTransform(CircleCrop()))
        }
        config?.placeHolder?.let {
            builder.placeholder(it)
        }
        builder.into(view)
    }

    private fun isNetworkImage(url: String): Boolean {
        return url.startsWith("http://") || url.startsWith("https://")
    }

    override fun load(url: String, target: ImageView, config: ImageLoader.Config?) {
        val requestManager = when {
            contextHolder.fragment != null -> {
                Glide.with(contextHolder.fragment!!)
            }
            contextHolder.activity != null -> {
                Glide.with(contextHolder.activity!!)
            }
            contextHolder.fragmentActivity != null -> {
                Glide.with(contextHolder.fragmentActivity!!)
            }
            contextHolder.context != null -> {
                Glide.with(contextHolder.context!!)
            }
            else -> {
                throw Exception("requestManager can not be null")
            }
        }

        setup(url, config, requestManager, target)
    }

    companion object: ImageLoader.Factory {
        override fun get(context: Context): ImageLoader {
            return GlideImageLoader(ImageLoader.ContextHolder(context))
        }

        override fun get(fragment: Fragment): ImageLoader {
            return GlideImageLoader(ImageLoader.ContextHolder(fragment))

        }

        override fun get(fragment: FragmentActivity): ImageLoader {
            return GlideImageLoader(ImageLoader.ContextHolder(fragment))
        }

        override fun get(activity: Activity): ImageLoader {
            return GlideImageLoader(ImageLoader.ContextHolder(activity))
        }
    }
}