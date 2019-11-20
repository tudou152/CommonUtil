package com.lliujun.utils.util

import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*
import kotlin.math.abs

//自动生成名字（中文）
fun getRandomJianHan(len: Int): String {
    var ret = ""
    for (i in 0 until len) {
        var str: String? = null
        val hightPos: Int
        val lowPos: Int // 定义高低位
        val random = Random()
        hightPos = 176 + abs(random.nextInt(39)) // 获取高位值
        lowPos = 161 + abs(random.nextInt(93)) // 获取低位值
        val b = ByteArray(2)
        b[0] = hightPos.toByte()
        b[1] = lowPos.toByte()
        try {
            str = String(b, Charset.forName("GBK")) // 转成中文
        } catch (ex: UnsupportedEncodingException) {
            ex.printStackTrace()
        }
        ret += str
    }
    return ret
}

//生成随机用户名，数字和字母组成,
fun getStringRandom(length: Int): String {

    var result = ""
    val random = Random()

    //参数length，表示生成几位随机数
    for (i in 0 until length) {

        val charOrNum = if (random.nextInt(2) % 2 == 0) "char" else "num"
        //输出字母还是数字
        if ("char".equals(charOrNum, ignoreCase = true)) {
            //输出是大写字母还是小写字母
            val temp = if (random.nextInt(2) % 2 == 0) 65 else 97
            result += (random.nextInt(26) + temp).toChar()
        } else if ("num".equals(charOrNum, ignoreCase = true)) {
            result += random.nextInt(10)
        }
    }
    return result
}

