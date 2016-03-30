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
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import com.benny.library.kbinding.adapterview.bindings.adapter
import com.benny.library.kbinding.adapterview.bindings.itemClick
import com.benny.library.kbinding.adapterview.converter.ListToRecyclerAdapterConverter
import com.benny.library.kbinding.common.bindings.fadeOut
import com.benny.library.kbinding.common.bindings.until
import com.benny.library.kbinding.dsl.bind
import com.benny.library.kbinding.dsl.wait
import com.benny.library.kbinding.view.ViewBinderComponent
import com.rayfantasy.icode.R
import com.rayfantasy.icode.extension.appBarLayout
import com.rayfantasy.icode.extension.colorAttr
import com.rayfantasy.icode.extension.dimenAttr
import com.rayfantasy.icode.extension.lparams
import com.rayfantasy.icode.ui.activity.CodeDetailActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView

class CodeDetailActivityUI : ViewBinderComponent<CodeDetailActivity> {
    override fun builder(): AnkoContext<*>.() -> Unit = {
        val activity = owner as CodeDetailActivity
        coordinatorLayout {
            appBarLayout(R.style.AppTheme_AppBarOverlay) {
                toolbar {
                    activity.setSupportActionBar(this)
                    minimumHeight = dimenAttr(R.attr.actionBarSize)
                    fitsSystemWindows = true
                    popupTheme = R.style.AppTheme_PopupOverlay
                }.lparams(matchParent, wrapContent) {
                    scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                            AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                }
            }.lparams(matchParent, wrapContent)

            frameLayout {
                backgroundColor = colorAttr(android.R.attr.colorBackground)
                recyclerView {
                    layoutManager = LinearLayoutManager(ctx)
                    bind {
                        adapter("blocks", converter = ListToRecyclerAdapterConverter(
                                BlocksViewCreator(CodeBlockView(activity.highlightTheme), activity.bindingDisposer)))
                    }
                    bind { itemClick("itemClick") }
                }.lparams(matchParent, matchParent)

                frameLayout {
                    backgroundColor = colorAttr(android.R.attr.colorBackground)
                    progressBar().lparams { gravity = Gravity.CENTER }
                    wait { until("blocks") { fadeOut() } }
                }.lparams(matchParent, matchParent)
            }.lparams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        }.lparams(matchParent, matchParent)
    }
}