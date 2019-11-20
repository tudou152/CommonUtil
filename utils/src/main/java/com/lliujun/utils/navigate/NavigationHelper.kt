package com.lliujun.utils.navigate

import android.content.Context
import android.content.pm.PackageManager
import android.content.Intent
import android.net.Uri

/**
 * 导航类,封装了室外导航的部分
 * */
@Suppress("SpellCheckingInspection", "unused")
class NavigationHelper {

    private fun checkApkExist(context: Context, packageName: String?): Boolean {
        if (packageName == null || "" == packageName)
            return false

        return try {
            val info = context.packageManager.getApplicationInfo(
                packageName,
                PackageManager.GET_UNINSTALLED_PACKAGES
            )
            return info.packageName == packageName
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    companion object {

        @JvmStatic
        fun startOutdoorNavigation(context: Context, name: String, lat: Double, lng: Double) {
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

        @JvmStatic
        fun startOutdoorNavigationWithBaidu(
            context: Context,
            name: String,
            lat: Double,
            lng: Double
        ) {
            // 驾车路线规划
            context.startActivity(Intent().apply {
                data = Uri.parse("baidumap://map/direction?destination=$name|$lat,$lng&coord_type=wgs84&mode=driving&src=android.geoxing")
            })
        }
    }
}