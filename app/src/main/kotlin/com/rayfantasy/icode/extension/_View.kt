package com.rayfantasy.icode.extension

import android.support.design.widget.Snackbar
import android.view.View

public fun View.snackBar(resId: Int, duration: Int = Snackbar.LENGTH_SHORT) = Snackbar.make(this, resId, duration).show()
public fun View.snackBar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT) = Snackbar.make(this, text, duration).show()
