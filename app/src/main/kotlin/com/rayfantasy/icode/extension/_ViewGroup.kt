package com.rayfantasy.icode.extension

import android.view.ViewGroup
import org.jetbrains.anko.layoutInflater

public fun ViewGroup.inflate(layoutRes: Int, attachToRoot: Boolean = false)
        = context.layoutInflater.inflate(layoutRes, this, attachToRoot)