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
import com.benny.library.kbinding.view.ViewBinderComponent
import com.rayfantasy.icode.R
import com.rayfantasy.icode.extension.appBarLayout
import com.rayfantasy.icode.extension.dimenAttr
import com.rayfantasy.icode.extension.lparams
import com.rayfantasy.icode.theme.ThemeModel
import com.rayfantasy.icode.theme.observe
import com.rayfantasy.icode.ui.activity.ActivityBindingBase
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.design._CoordinatorLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

abstract class AppBarActivityUI<T : ActivityBindingBase> : ViewBinderComponent<T> {
    override fun builder(): AnkoContext<*>.() -> Unit = {
        val activity = owner as ActivityBindingBase
        val ankoContext = this
        coordinatorLayout {
            appBarLayout(R.style.AppTheme_AppBarOverlay) {
                toolbar {
                    activity.setSupportActionBar(this)
                    minimumHeight = dimenAttr(R.attr.actionBarSize)
                    fitsSystemWindows = true
                    popupTheme = R.style.AppTheme_PopupOverlay
                    observe(ThemeModel.colorPrimary) {
                        backgroundColor = it
                    }
                }.lparams(matchParent, wrapContent) {
                    scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                            AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                }
            }.lparams(matchParent, wrapContent)

            content(this)(ankoContext)

        }.lparams(matchParent, matchParent)
    }

    abstract fun content(parent: _CoordinatorLayout): AnkoContext<*>.() -> Unit
}