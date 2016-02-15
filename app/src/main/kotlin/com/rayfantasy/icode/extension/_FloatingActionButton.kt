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

import android.support.design.widget.FloatingActionButton
import android.view.animation.DecelerateInterpolator

fun FloatingActionButton.toggle(visible: Boolean, duration: Long) {
    val translationY = (if (visible) 0 else height + getMarginBottom()).toFloat()
    if (duration != 0L)
        animate()
                .translationY(translationY)
                .setDuration(duration)
                .setInterpolator(DecelerateInterpolator())
                .start()
    else
        setTranslationY(translationY)
}
