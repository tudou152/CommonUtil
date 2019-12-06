package com.lliujun.utils.util.imageloader

import android.content.Context
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.lliujun.utils.util.imageloader.glide.GlideImageLoader

object ImageLoaderManager : ImageLoader {
    private val imageLoader = GlideImageLoader()

    override fun load(activity: AppCompatActivity, url: String, view: ImageView, config: ImageLoader.Config?) {
        imageLoader.load(activity, url, view, config)
    }

    override fun load(fragment: Fragment, url: String, view: ImageView, config: ImageLoader.Config?) {
        imageLoader.load(fragment, url, view, config)
    }

    override fun load(fragment: FragmentActivity, url: String, view: ImageView, config: ImageLoader.Config?) {
        imageLoader.load(fragment, url, view, config)
    }

    override fun load(context: Context, url: String, view: ImageView, config: ImageLoader.Config?) {
        imageLoader.load(context, url, view, config)
    }
}
