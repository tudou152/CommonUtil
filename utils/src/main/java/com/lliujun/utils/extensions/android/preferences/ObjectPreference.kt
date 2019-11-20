package com.lliujun.utils.extensions.android.preferences

import android.content.SharedPreferences
import android.util.Base64
import androidx.annotation.WorkerThread
import java.io.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ObjectPreference<T : Serializable>(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Any?
) : ReadWriteProperty<Any, T?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        val temp = preferences.value.getString(name, "")
        if (temp.isNullOrEmpty()) return defaultValue as T

        val arrayInputStream = ByteArrayInputStream(
            Base64.decode(
                temp.toByteArray(),
                Base64.DEFAULT
            )
        )
        var result: T? = null

        try {
            val ois = ObjectInputStream(arrayInputStream)
            result = ois.readObject() as? T
            arrayInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return result ?: defaultValue as? T
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        preferences.value.edit {
            try {
                val outputStream = ByteArrayOutputStream()
                val oos = ObjectOutputStream(outputStream)
                //把对象写到流里
                oos.writeObject(value)
                val temp = String(Base64.encode(outputStream.toByteArray(), Base64.DEFAULT))
                putString(name, temp)
                oos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}