package com.lliujun.utils.navigate

import android.content.Context
import android.content.Intent
import android.net.Uri

class BaiduNavigation: INavigation {
    override fun startNavigation(context: Context, name: String, lat: Double, lng: Double) {
        // 驾车路线规划
        context.startActivity(Intent().apply {
            data = Uri.parse("baidumap://map/direction?destination=$name|$lat,$lng&coord_type=wgs84&mode=driving&src=android.geoxing")
        })
    }
}