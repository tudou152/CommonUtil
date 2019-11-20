package com.lliujun.utils.util

import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.util.Log

fun Context.isLocationServiceEnabled(): Boolean {
    val locationManager: LocationManager =
        getSystemService(Context.LOCATION_SERVICE) as LocationManager

    val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
        Log.i("locationServiceEnable","当前定位服务可用, gps: $gps, network: $network")
        locationManager.isLocationEnabled
    } else {
        gps || network
    }
}
