package com.lliujun.utils.navigate

import android.content.Context
import android.content.Intent
import android.net.Uri

class GaodeNavigation : INavigation {
    override fun startNavigation(context: Context, name: String, lat: Double, lng: Double) {
        val stringBuffer = StringBuffer("androidamap://route?sourceApplication=").append("amap")

        stringBuffer.append("&dlat=").append(lat)
            .append("&dlon=").append(lng)
            .append("&dname=").append(name)
            .append("&dev=").append(0)
            .append("&t=").append(0)

        val intent =
            Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()))
        intent.setPackage("com.autonavi.minimap")
        context.startActivity(intent)
    }
}