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

import com.nineoldandroids.animation.Animator

fun Animator.addListener(init: _AnimatorListenerNineOldAndroids.() -> Unit)
        = addListener(_AnimatorListenerNineOldAndroids().apply(init))

class _AnimatorListenerNineOldAndroids : Animator.AnimatorListener {
    private var _onAnimationRepeat: ((Animator) -> Unit)? = null
    private var _onAnimationEnd: ((Animator) -> Unit)? = null
    private var _onAnimationCancel: ((Animator) -> Unit)? = null
    private var _onAnimationStart: ((Animator) -> Unit)? = null

    override fun onAnimationRepeat(p0: Animator) {
        _onAnimationRepeat?.invoke(p0)
    }

    override fun onAnimationEnd(p0: Animator) {
        _onAnimationEnd?.invoke(p0)
    }

    override fun onAnimationCancel(p0: Animator) {
        _onAnimationCancel?.invoke(p0)
    }

    override fun onAnimationStart(p0: Animator) {
        _onAnimationStart?.invoke(p0)
    }

    fun onAnimationRepeat(listener: (Animator) -> Unit) {
        _onAnimationRepeat = listener
    }

    fun onAnimationEnd(listener: (Animator) -> Unit) {
        _onAnimationEnd = listener
    }

    fun onAnimationCancel(listener: (Animator) -> Unit) {
        _onAnimationCancel = listener
    }

    fun onAnimationStart(listener: (Animator) -> Unit) {
        _onAnimationStart = listener
    }
}