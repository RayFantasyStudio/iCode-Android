package com.rayfantasy.icode.extension

import android.view.ViewGroup
import org.jetbrains.anko.layoutInflater

fun ViewGroup.inflate(layoutRes: Int, attachToRoot: Boolean = false)
        = context.layoutInflater.inflate(layoutRes, this, attachToRoot)