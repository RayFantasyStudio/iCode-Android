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
import com.benny.library.kbinding.adapterview.bindings.adapter
import com.benny.library.kbinding.adapterview.converter.ListToRecyclerAdapterConverter
import com.benny.library.kbinding.adapterview.viewcreator.ItemViewBinderComponent
import com.benny.library.kbinding.adapterview.viewcreator.viewCreator
import com.benny.library.kbinding.adapterview.viewmodel.ItemViewModel
import com.benny.library.kbinding.annotation.DependencyProperty
import com.benny.library.kbinding.annotation.Property
import com.benny.library.kbinding.common.bindings.text
import com.benny.library.kbinding.dsl.bind
import com.benny.library.kbinding.view.ViewBinderComponent
import com.benny.library.kbinding.view.setContentView
import com.ferencboldog.ankomaterial.extensions.*
import com.rayfantasy.icode.R
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.CodeGood
import com.rayfantasy.icode.postutil.extension.fromJson
import com.rayfantasy.icode.postutil.extension.loadCodeContent
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.recyclerview.v7.recyclerView
import kotlin.properties.Delegates

class CodeDetailActivity : ActivityBindingBase() {

    @delegate:Property
    var blocks: List<CodeGood.Block>? by Delegates.property()

    val codeGood by lazy { intent.getSerializableExtra("codeGood") as CodeGood }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CodeDetailActivityUI().setContentView(this).bindTo(this)
        loadItem()
    }

    fun loadItem() {
        //codeGood.loadContentFromCache()
        //codeGood.content?.let {
        //    blocks = PostUtil.gson.fromJson(codeGood.content)
        //}
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

    inner class CodeDetailActivityUI : ViewBinderComponent<CodeDetailActivity> {
        override fun builder(): AnkoContext<*>.() -> Unit = {
            fitedCoordinatorLayout {
                appBarLayout(R.style.AppTheme_AppBarOverlay) {
                    toolbar {
                        setSupportActionBar(this)
                        minimumHeight = dimenAttr(R.attr.actionBarSize)
                        fitsSystemWindows = true
                        popupTheme = R.style.AppTheme_PopupOverlay
                    }
                }.lparams(matchParent, wrapContent)

                recyclerView {
                    backgroundColor = colorAttr(android.R.attr.colorBackground)
                    layoutManager = LinearLayoutManager(ctx)
                    bind {
                        adapter("blocks", converter = ListToRecyclerAdapterConverter(
                                (owner as CodeDetailActivity).viewCreator(BlockView(), ::BlockViewModel)))
                    }
                }.lparams(matchParent, matchParent) {
                    behavior = AppBarLayout.ScrollingViewBehavior()
                }
            }.lparams(matchParent, matchParent)
        }
    }

    class BlockView : ItemViewBinderComponent {
        override fun builder(): AnkoContext<*>.() -> Unit = {
            textView {
                bind { text("content") }
            }
        }
    }

    class BlockViewModel : ItemViewModel<CodeGood.Block>() {
        @delegate:Property
        var block by Delegates.property<CodeGood.Block>()

        @delegate:DependencyProperty("block")
        val content by Delegates.property { block?.content }

        override fun onDataChange(data: CodeGood.Block?) {
            block = data
        }
    }
}