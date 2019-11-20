package com.lliujun.utils.extensions

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive


fun JsonObject.getAsJsonPrimitiveOrNull(memberName: String) : JsonPrimitive? {
    return if (has(memberName)) {
        val obj = get(memberName)
        if (obj.isJsonPrimitive) obj.asJsonPrimitive else null
    } else {
        null
    }
}

fun JsonObject.getAsJsonArrayOrNull(memberName: String) : JsonArray? {
    return if (has(memberName)) {
        val obj = get(memberName)
        if (obj.isJsonArray) obj.asJsonArray else null
    } else {
        return null
    }
}

fun JsonObject.getAsJsonObjectOrNull(memberName: String) : JsonObject? {
    return if (has(memberName)) {
        val obj = get(memberName)
        if (obj.isJsonObject) obj.asJsonObject else null
    } else {
        null
    }
}