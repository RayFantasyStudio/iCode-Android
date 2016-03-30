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

package com.rayfantasy.icode.ui.layout

import android.support.design.widget.AppBarLayout
import com.rayfantasy.icode.extension._RevealFrameLayout
import com.rayfantasy.icode.extension.revealFrameLayout
import com.rayfantasy.icode.ui.activity.ActivityBindingBase
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.design._CoordinatorLayout
import org.jetbrains.anko.matchParent

abstract class FabTransformActivityUI<T : ActivityBindingBase> : AppBarActivityUI<T>() {
    override fun content(parent: _CoordinatorLayout): AnkoContext<*>.() -> Unit = {
        with(parent) {
            revealFrameLayout {
                revealContent(this)()
            }.lparams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        }
    }

    abstract fun revealContent(parent: _RevealFrameLayout): AnkoContext<*>.() -> Unit
}