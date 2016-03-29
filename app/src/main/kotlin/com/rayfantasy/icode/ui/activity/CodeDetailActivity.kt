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

package com.rayfantasy.icode.ui.activity

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import com.benny.library.autoadapter.viewcreator.ViewCreatorCollection
import com.benny.library.kbinding.adapterview.bindings.adapter
import com.benny.library.kbinding.adapterview.bindings.itemClick
import com.benny.library.kbinding.adapterview.converter.ListToRecyclerAdapterConverter
import com.benny.library.kbinding.adapterview.viewcreator.ItemViewBinderComponent
import com.benny.library.kbinding.annotation.Command
import com.benny.library.kbinding.annotation.Property
import com.benny.library.kbinding.bind.BindingDisposer
import com.benny.library.kbinding.common.bindings.fadeOut
import com.benny.library.kbinding.common.bindings.text
import com.benny.library.kbinding.common.bindings.until
import com.benny.library.kbinding.dsl.bind
import com.benny.library.kbinding.dsl.wait
import com.benny.library.kbinding.view.ViewBinderComponent
import com.benny.library.kbinding.view.setContentView
import com.ferencboldog.ankomaterial.extensions.appBarLayout
import com.ferencboldog.ankomaterial.extensions.colorAttr
import com.ferencboldog.ankomaterial.extensions.dimenAttr
import com.ferencboldog.ankomaterial.extensions.lparams
import com.rayfantasy.icode.R
import com.rayfantasy.icode.extension.highlightEditText
import com.rayfantasy.icode.extension.lang
import com.rayfantasy.icode.extension.source
import com.rayfantasy.icode.extra.ViewCreator
import com.rayfantasy.icode.model.BlockViewModel
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.CodeGood
import com.rayfantasy.icode.postutil.extension.fromJson
import com.rayfantasy.icode.postutil.extension.loadCodeContent
import com.rayfantasy.icode.ui.fragment.SettingFragment
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView
import kotlin.properties.Delegates

class CodeDetailActivity : ActivityBindingBase() {
    override val bindingStatus: Boolean
        get() = true

    @delegate:Property
    var blocks: List<CodeGood.Block>? by Delegates.property()

    private val codeGood by lazy { intent.getSerializableExtra("codeGood") as CodeGood }
    private val highlightTheme by lazy {
        defaultSharedPreferences
                .getString(SettingFragment.PREF_HIGHLIGHT, SettingFragment.DEFAULT_HIGHLIGHT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CodeDetailActivityUI().setContentView(this).bindTo(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        loadItem()
    }

    fun loadItem() {
        codeGood.loadContentFromCache()
        codeGood.content?.let {
            blocks = PostUtil.gson.fromJson(codeGood.content)
        }
        codeGood.loadCodeContent {
            onSuccess {
                blocks = PostUtil.gson.fromJson(it!!)
            }
            onFailed { throwable, rc ->
                toast("rc = $rc")
                throwable.printStackTrace()
            }
        }
    }

    @Command
    fun itemClick(params: Int) {
    }

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

    class TextBlockView : ItemViewBinderComponent {
        override fun builder(): AnkoContext<*>.() -> Unit = {
            textView {
                bind { text("content") }
            }.lparams(matchParent, wrapContent) {
                margin = dip(8)
            }
        }
    }

    class CodeBlockView(val theme: String) : ItemViewBinderComponent {
        override fun builder(): AnkoContext<*>.() -> Unit = {
            highlightEditText(theme) {
                bind { source("content") }
                bind { lang("extra") }
            }.lparams(matchParent, wrapContent) {
                margin = dip(8)
            }
        }
    }

    class BlocksViewCreator(codeBlockView: CodeBlockView, val bindingDisposer: BindingDisposer) : ViewCreatorCollection<CodeGood.Block>() {
        init {
            addFilter({ data, position, itemCount -> data.blockType == CodeGood.BlockType.TEXT },
                    ViewCreator(bindingDisposer, TextBlockView(), ::BlockViewModel))
            addFilter({ data, position, itemCount -> data.blockType == CodeGood.BlockType.CODE }, ViewCreator(bindingDisposer,
                    codeBlockView, ::BlockViewModel))
        }
    }
}