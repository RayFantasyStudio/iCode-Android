package com.rayfantasy.icode.postutil.extension

import com.android.volley.Request
import com.android.volley.RequestQueue

fun RequestQueue.cancelAll(request: Request<*>) = cancelAll(RequestQueue.RequestFilter { it === request })