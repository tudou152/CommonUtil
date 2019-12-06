package com.lliujun.utils.util.imageloader.glide

import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.lliujun.utils.util.imageloader.ImageLoader

class GlideImageLoader : ImageLoader {
    override fun load(activity: AppCompatActivity, url: String, view: ImageView, config: ImageLoader.Config?) {
        setup(url, config, Glide.with(activity), view)
    }

    override fun load(fragment: FragmentActivity, url: String, view: ImageView, config: ImageLoader.Config?) {
        setup(url, config, Glide.with(fragment), view)
    }

    override fun load(fragment: Fragment, url: String, view: ImageView, config: ImageLoader.Config?) {
        setup(url, config, Glide.with(fragment), view)
    }

    override fun load(context: Context, url: String, view: ImageView, config: ImageLoader.Config?) {
        setup(url, config, Glide.with(context), view)
    }

    private fun setup(url: String, config: ImageLoader.Config?, requestManager: RequestManager, view: ImageView) {
        var builder = if (isNetworkImage(url) && url.endsWith(".svg")) {
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
}