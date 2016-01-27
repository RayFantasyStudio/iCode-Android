package com.rayfantasy.icode.postutil.extension

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

public inline fun <reified T : Any> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)
