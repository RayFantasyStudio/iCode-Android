/*
 * Copyright 2015 Alex Zhang aka. ztc1997
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/*package com.rayfantasy.icode.util

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.util.Pair
import android.view.View
import android.view.Window
import com.rayfantasy.icode.R
import java.util.*

fun startActivity(activity: Activity, intent: Intent, vararg pairs: Pair<out View, String>) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        val pairList = ArrayList(Arrays.asList(*pairs))

        val statusbar = activity.findViewById(android.R.id.statusBarBackground)
        if (statusbar != null)
            pairList.add(Pair.create(statusbar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME))

        val navbar = activity.findViewById(android.R.id.navigationBarBackground)
        if (navbar != null)
            pairList.add(Pair.create(navbar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME))

        val options = ActivityOptions.makeSceneTransitionAnimation(activity, pairList.toArray<Pair<out View, out View>>(arrayOfNulls<Pair<out View, out View>>(pairList.size)))
        activity.startActivity(intent, options.toBundle())
    } else {
        activity.startActivity(intent)
    }
    activity.overridePendingTransition(R.anim.slide_up, R.anim.scale_down)
}

fun dip2px(context: Context, dipValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dipValue * scale + 0.5f).toInt()
}

fun px2dip(context: Context, pxValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}*/

