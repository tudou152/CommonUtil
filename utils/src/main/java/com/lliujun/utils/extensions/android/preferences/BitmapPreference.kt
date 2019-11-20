package com.lliujun.utils.extensions.android.preferences

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.annotation.WorkerThread
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class BitmapPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Bitmap?
) : ReadWriteProperty<Any, Bitmap?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Bitmap? {
        val temp = preferences.value.getString(name, "")

        if (!temp.isNullOrEmpty()) {
            val byteArrayInputStream = ByteArrayInputStream(
                Base64.decode(
                    temp.toByteArray(),
                    Base64.DEFAULT
                )
            )
            return BitmapFactory.decodeStream(byteArrayInputStream)
        }

        return defaultValue
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Bitmap?) {
        preferences.value.edit {
            val byteArrayOutputStream = ByteArrayOutputStream()
            value?.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream)
            val imageBase64 =
                Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
            putString(name, imageBase64)
        }
    }
}