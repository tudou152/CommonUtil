package com.lliujun.utils.util

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresPermission

/**
 * 手机震动
 */
@RequiresPermission(Manifest.permission.VIBRATE)
fun Context.vibrate() {
    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val ve = VibrationEffect.createOneShot(500,
            VibrationEffect.DEFAULT_AMPLITUDE)
        vibrator.vibrate(ve)
    } else {
        vibrator.vibrate(500)
    }
}