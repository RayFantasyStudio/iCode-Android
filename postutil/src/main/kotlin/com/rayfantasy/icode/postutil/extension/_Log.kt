package com.rayfantasy.icode.postutil.extension

import android.util.Log

const val DEBUG = true

fun Any.v(msg: () -> String) {
    if (DEBUG)
        if (Log.isLoggable(tag, Log.VERBOSE)) v(msg())
}

fun Any.d(msg: () -> String) {
    if (DEBUG)
        if (Log.isLoggable(tag, Log.DEBUG)) d(msg())
}

fun Any.i(msg: () -> String) {
    if (DEBUG)
        if (Log.isLoggable(tag, Log.INFO)) i(msg())
}

fun Any.e(msg: () -> String) {
    if (DEBUG)
        if (Log.isLoggable(tag, Log.ERROR)) e(msg())
}

fun Any.wtf(msg: () -> String) {
    w(msg())
}

fun Any.v(msg: String) {
    v(tag, msg)
}

fun Any.d(msg: String) {
    d(tag, msg)
}

fun Any.i(msg: String) {
    i(tag, msg)
}

fun Any.w(msg: String) {
    w(tag, msg)
}

fun Any.e(msg: String) {
    e(tag, msg)
}

fun Any.wtf(msg: String) {
    wtf(tag, msg)
}

fun Any.v(tag: String, msg: String) {
    if (DEBUG)
        Log.v(tag, msg)
}

fun Any.d(tag: String, msg: String) {
    if (DEBUG)
        Log.d(tag, msg)
}

fun Any.i(tag: String, msg: String) {
    if (DEBUG)
        Log.i(tag, msg)
}

fun Any.w(tag: String, msg: String) {
    if (DEBUG)
        Log.w(tag, msg)
}

fun Any.e(tag: String, msg: String) {
    if (DEBUG)
        Log.e(tag, msg)
}

fun Any.wtf(tag: String, msg: String) {
    if (DEBUG)
        Log.wtf(tag, msg)
}

private val Any.tag: String
    get() = javaClass.simpleName