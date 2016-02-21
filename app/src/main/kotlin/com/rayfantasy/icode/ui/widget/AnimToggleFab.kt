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

package com.rayfantasy.icode.ui.widget

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.view.ViewPropertyAnimator
import android.view.animation.DecelerateInterpolator
import com.rayfantasy.icode.extension.getMarginBottom
import com.rayfantasy.icode.extension.setListener

class AnimToggleFab : FloatingActionButton {
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var animator: ViewPropertyAnimator? = null
    private var show = true

    fun toggle(visible: Boolean, duration: Long) {
        if (show == visible) return
        show = visible
        val translationY = (if (visible) 0 else height + getMarginBottom()).toFloat()
        animator?.let { it.cancel() }
        if (duration != 0L) {
            animator = animate()
                    .translationY(translationY)
                    .setDuration(duration)
                    .setInterpolator(DecelerateInterpolator())
                    .setListener { onAnimationEnd { animator = null } }
            (animator as ViewPropertyAnimator).start()
        } else
            setTranslationY(translationY)
    }
}