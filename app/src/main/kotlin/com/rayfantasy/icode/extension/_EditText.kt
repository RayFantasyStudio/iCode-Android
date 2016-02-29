package com.rayfantasy.icode.extension

import android.widget.EditText

var EditText.string: String
    get() = text.toString()
    set(value) = setText(value)