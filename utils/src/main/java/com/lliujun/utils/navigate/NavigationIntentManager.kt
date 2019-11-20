@file:Suppress("SpellCheckingInspection")

package com.lliujun.utils.navigate

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.lliujun.utils.util.isAvailable

@Suppress("unused")
class NavigationIntentManager {
    companion object {

        @JvmStatic
        fun getAvailableNavigationIntents(
            context: Context,
            ename: String,
            elat: Double,
            elng: Double
        ): MutableList<IntentModel> {
            val availableIntents = mutableListOf<IntentModel>()

            val gaodeIntent = getGaodeNavigationIntent(ename, elat, elng)
            if (gaodeIntent.isAvailable(context)) {
                availableIntents.add(IntentModel("高德地图", GaodeNavigation()))
            }
            val baiduIntent = getBaiduNavigationIntent(ename, elat, elng)
            if (baiduIntent.isAvailable(context)) {
                availableIntents.add(IntentModel("百度地图", BaiduNavigation()))
            }
            return availableIntents
        }


        @JvmStatic
        fun getGaodeNavigationIntent(name: String, lat: Double, lng: Double): Intent {
            val stringBuffer = StringBuffer("androidamap://route?sourceApplication=").append("amap")

            stringBuffer.append("&dlat=").append(lat)
                .append("&dlon=").append(lng)
                .append("&dname=").append(name)
                .append("&dev=").append(0)
                .append("&t=").append(0)

            val intent =
                Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()))
            intent.setPackage("com.autonavi.minimap")
            return intent
        }

        @JvmStatic
        fun getBaiduNavigationIntent(name: String, lat: Double, lng: Double): Intent {
            // 驾车路线规划
            return Intent().apply {
                data =
                    Uri.parse("baidumap://map/direction?destination=$name|$lat,$lng&coord_type=wgs84&mode=driving&src=android.geoxing")
            }
        }

        /**
         * 打电话
         * */
        @JvmStatic
        fun callPhone(context: Context, number: String, isDial: Boolean = false) {
            val action = if (isDial) {
                Intent.ACTION_DIAL
            } else {
                Intent.ACTION_CALL
            }
            context.startActivity(Intent(action).apply {
                data = Uri.parse("tel:$number")
            })
        }
    }
}
