package com.lliujun.utils.extensions.android

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Activity.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
            this,
            permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun Fragment.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
            context!!,
            permission
    ) == PackageManager.PERMISSION_GRANTED
}

inline fun <reified T: Activity> Activity.startActivity() {
    startActivity(Intent(this, T::class.java))
}

inline fun <reified T: Activity> Activity.startActivityForResult(requestCode: Int, bundle: Bundle? = null) {
    startActivityForResult(Intent(this, T::class.java), requestCode, bundle)
}