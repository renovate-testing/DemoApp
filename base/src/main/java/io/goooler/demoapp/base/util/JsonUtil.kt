package io.goooler.demoapp.base.util

import com.alibaba.fastjson.JSONException
import com.alibaba.fastjson.JSONObject

/**
 * 对 fastJson 的简单封装，以便统一异常处理等操作
 */
object JsonUtil {

    fun <T> fromJson(jsonString: String, clazz: Class<T>): T? {
        return try {
            JSONObject.parseObject(jsonString, clazz)
        } catch (e: JSONException) {
            LogUtil.d(e.toString())
            null
        }
    }

    fun toJson(o: Any): String {
        return JSONObject.toJSONString(o)
    }
}

inline fun <reified T> String.fromJson(): T? {
    return JsonUtil.fromJson(this, T::class.java)
}

fun Any.toJson(): String {
    return JsonUtil.toJson(this)
}