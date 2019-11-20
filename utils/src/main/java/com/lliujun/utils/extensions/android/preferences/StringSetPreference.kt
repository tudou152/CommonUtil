package com.lliujun.utils.extensions.android.preferences

import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class StringSetPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Set<String>?
) : ReadWriteProperty<Any, Set<String>?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Set<String>? {
        return preferences.value.getStringSet(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Set<String>?) {
        preferences.value.edit { putStringSet(name, value) }
    }
}