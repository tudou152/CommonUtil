package com.lliujun.utils.util.imageloader.glide

import android.graphics.Picture
import android.graphics.drawable.PictureDrawable
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.SimpleResource
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.Target
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import java.io.IOException
import java.io.InputStream


/**
 * Decodes an SVG internal representation from an [InputStream].
 */
class SvgDecoder : ResourceDecoder<InputStream?, SVG?> {

    override fun handles(source: InputStream, options: Options): Boolean {
        return true
    }

    override fun decode(
        source: InputStream,
        width: Int,
        height: Int,
        options: Options
    ): Resource<SVG?>? {
        return try {
        val svg = SVG.getFromInputStream(source)
        SimpleResource(svg)
    } catch (ex: SVGParseException) {
        throw IOException("Cannot load SVG from stream", ex)
    }
    }
}

/**
 * Convert the [SVG]'s internal representation to an Android-compatible one
 * ([Picture]).
 */
class SvgDrawableTransCoder : ResourceTranscoder<SVG?, PictureDrawable> {

    override fun transcode(
        toTranscode: Resource<SVG?>,
        options: Options
    ): Resource<PictureDrawable>? {
        val svg = toTranscode.get()
        val picture: Picture = svg.renderToPicture()
        val drawable = PictureDrawable(picture)
        return SimpleResource(drawable)
    }
}


/**
 * Listener which updates the [ImageView] to be software rendered, because
 * [SVG][com.caverock.androidsvg.SVG]/[Picture][android.graphics.Picture] can't render on
 * a hardware backed [Canvas][android.graphics.Canvas].
 */
class SvgSoftwareLayerSetter : RequestListener<PictureDrawable?> {
    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<PictureDrawable?>,
                              isFirstResource: Boolean): Boolean {
        val view = (target as ImageViewTarget<*>).view
        view.setLayerType(ImageView.LAYER_TYPE_NONE, null)
        return false
    }

    override fun onResourceReady(resource: PictureDrawable?, model: Any,
                                 target: Target<PictureDrawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
        val view = (target as ImageViewTarget<*>).view
        view.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null)
        return false
    }
}