package com.lliujun.utils.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

/**
 * 获取版本名称
 */
fun Context.getVersionName(): String? {
    //获取包信息
    try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        //返回版本号
        return packageInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return null
}

/**
 * 获取App的名称
 */
fun Context.getAppName(): String? {
    val pm = packageManager
    //获取包信息
    try {
        val packageInfo = pm.getPackageInfo(packageName, 0)
        //获取应用 信息
        val applicationInfo = packageInfo.applicationInfo
        //获取albelRes
        val labelRes = applicationInfo.labelRes
        //返回App的名称
        return resources.getString(labelRes)
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return null
}

/**
 * 判断该 Intent 是否可以被处理
 * */
fun Intent.isAvailable(context: Context): Boolean {
    return resolveActivity(context.packageManager) != null
}