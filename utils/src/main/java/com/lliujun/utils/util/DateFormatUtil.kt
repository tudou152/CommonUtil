@file:Suppress("MemberVisibilityCanBePrivate")

package com.lliujun.utils.util

import java.text.SimpleDateFormat
import java.util.*

object DateFormatUtil {

    private const val DATE_PATTERN_1 = "yyyy/MM/dd"
    private const val BIRTH_FORMAT = "yyyy年MM月dd日"

    fun format(date: Long): String {
        return format(Date(date))
    }

    fun format(date: Date): String {
        return format(DATE_PATTERN_1, date)
    }

    fun format(pattern: String, date: Long): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(date))
    }

    fun format(pattern: String, date: Date): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(date)
    }

    fun birthFormat(date: Long): String {
        return format(BIRTH_FORMAT, date)
    }

    fun birthFormat(date: Date): String {
        return format(BIRTH_FORMAT, date)
    }

    fun toHourMinute(time: Long): String {

        val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("GMT+00:00")
        }

        val temp = formatter.format(Date(time)).split(":")
        val hour = temp[0]
        val minute= temp[1]
        val second = temp[2]

        var result = ""
        if (hour.toInt() != 0) {
            result += "${hour.toInt()}小时"
        }

        if (minute.toInt() != 0) {
            result += "${minute.toInt()}分钟"
        }

        if (second.toInt() != 0) {
            result += "${second.toInt()}秒"
        }

        return result
    }
}

fun Long.toBirth(): String = DateFormatUtil.birthFormat(this)
fun Date.toBirth(): String = DateFormatUtil.birthFormat(this)
