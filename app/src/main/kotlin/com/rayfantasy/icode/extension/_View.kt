package com.rayfantasy.icode.extension

import android.support.design.widget.Snackbar
import android.view.View
import android.view.ViewGroup

fun View.snackBar(resId: Int, duration: Int = Snackbar.LENGTH_SHORT) = Snackbar.make(this, resId, duration).show()
fun View.snackBar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT) = Snackbar.make(this, text, duration).show()

fun View.getMarginBottom(): Int {
    var marginBottom = 0
    val layoutParams = layoutParams
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        marginBottom = layoutParams.bottomMargin;
    }
    return marginBottom
}