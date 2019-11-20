package com.lliujun.utils.util

import android.annotation.SuppressLint
import android.os.Build
import android.webkit.WebSettings
import android.webkit.WebView

/**
 * WebView通用设置
 */
@SuppressLint("SetJavaScriptEnabled")
fun initWebSettings(mWebView: WebView, progressListener: ((Int) -> Unit)? = null) {

    // 使用腾讯x5内核的时候, 避免调用setLayerType
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
//        } else {
//            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
//        }

    //允许调试
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        WebView.setWebContentsDebuggingEnabled(true)
    }

//    mWebView.webViewClient = MyWebViewClient()
//    mWebView.webChromeClient = MyWebChromeClient().apply {
//        mListener = object : MyWebChromeClient.OnProgressListener {
//            override fun onProgress(progress: Int) {
//                progressListener?.invoke(progress)
//            }
//        }
//    }

    val mWebSettings = mWebView.settings

    //如果访问的页面中要与JS交互，则WebView必须设置支持JS
    // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费(CPU、电量), 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
    mWebSettings.javaScriptEnabled = true
    mWebSettings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口，和windows.open()效果相同
//        mWebSettings.setSupportMultipleWindows(true);         //支持多窗口，默认为false

    mWebSettings.allowFileAccess = true //设置可以访问文件
    mWebSettings.defaultTextEncodingName = "utf-8"   //设置编码格式

    mWebSettings.loadsImagesAutomatically = true //支持自动加载图片
//        mWebSettings.setBlockNetworkImage(true);        //设置是否不加载网络图片
//        mWebSettings.pluginState = WebSettings.PluginState.ON               //是否启用插件(默认false，deprecated)

    //设置自适应屏幕
    mWebSettings.useWideViewPort = true          //调整到适合WebView的大小(html meta tag)
    mWebSettings.loadWithOverviewMode = true     //缩放至屏幕大小(和useWideViewPort一起用)
//        mWebSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN

    //设置缩放操作
    mWebSettings.setSupportZoom(true)     //支持缩放，默认为true.是下面的前提
    mWebSettings.builtInZoomControls = true     //设置内置的缩放控件.若为false，则该WebView不可缩放
    mWebSettings.displayZoomControls = false     //是否显示原生的缩放控件

    //优先使用缓存:
    //缓存模式如下：
    //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
    //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
    //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
    //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
    mWebSettings.cacheMode = WebSettings.LOAD_DEFAULT

    // 开启 DOM storage API 功能
    mWebSettings.domStorageEnabled = true

    //开启 database storage API 功能
    mWebSettings.databaseEnabled = true

    //开启 Application Caches 功能
    mWebSettings.setAppCacheEnabled(true)

    //当加载html页面时，WebView会在/data/data包名目录下生成database和cache两个文件夹
    //请求的URL记录保存在WebViewCache.db，而URL的内容保存在WebViewCache文件夹下

    //允许进行地理定位，默认为true
    mWebSettings.setGeolocationEnabled(true)

//        //Android 8.0以上
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            mWebSettings.safeBrowsingEnabled = false
//        }

    //允许Cookie和第三方Cookie认证
//        val cookieManager = CookieManager.getInstance()
//        cookieManager.setAcceptCookie(true)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            cookieManager.setAcceptThirdPartyCookies(mWebView, true)
//        }
}