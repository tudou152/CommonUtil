package com.lliujun.utils.navigate

import android.content.Context

interface INavigation {
    fun startNavigation(context: Context,name: String, lat: Double, lng: Double)
}