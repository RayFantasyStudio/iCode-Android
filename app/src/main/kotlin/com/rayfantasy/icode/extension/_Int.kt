/*
 * Copyright 2016 Alex Zhang aka. ztc1997
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

package com.rayfantasy.icode.extension

import android.graphics.Color

private const val COLOR_THRESHOLD = 256 * 2
private const val SHADE_FACTOR = 0.9

tailrec fun Int.shadowColor(): Int {
    val lightness = Color.red(this) + Color.green(this) + Color.blue(this)
    if (lightness < COLOR_THRESHOLD)
        return this
    else
        return Color.rgb((SHADE_FACTOR * Color.red(this)).toInt(),
                (SHADE_FACTOR * Color.green(this)).toInt(),
                (SHADE_FACTOR * Color.blue(this)).toInt()).shadowColor()
}

fun Int.alpha(alpha: Int) = Color.argb(alpha, Color.red(this), Color.green(this), Color.blue(this))