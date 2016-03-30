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

import com.benny.library.autoadapter.viewcreator.ViewCreatorCollection
import com.benny.library.kbinding.adapterview.viewcreator.ItemViewBinderComponent
import com.benny.library.kbinding.bind.BindingDisposer
import com.benny.library.kbinding.common.bindings.text
import com.benny.library.kbinding.dsl.bind
import com.ferencboldog.ankomaterial.extensions.lparams
import com.rayfantasy.icode.extension.block
import com.rayfantasy.icode.extra.ViewCreator
import com.rayfantasy.icode.model.BlockViewModel
import com.rayfantasy.icode.postutil.bean.CodeGood
import org.jetbrains.anko.*

class BlocksViewCreator(codeBlockView: CodeBlockView, val bindingDisposer: BindingDisposer) : ViewCreatorCollection<CodeGood.Block>() {
    init {
        addFilter({ data, position, itemCount -> data.blockType == CodeGood.BlockType.TEXT },
                com.rayfantasy.icode.extra.ViewCreator(bindingDisposer, TextBlockView(), ::BlockViewModel))
        addFilter({ data, position, itemCount -> data.blockType == CodeGood.BlockType.CODE }, ViewCreator(bindingDisposer,
                codeBlockView, ::BlockViewModel))
    }
}

class CodeBlockView(val theme: String) : ItemViewBinderComponent {
    override fun builder(): AnkoContext<*>.() -> Unit = {
        /*highlightEditText(theme) {
            keyListener = null
            editAble = false
            bind { source("content") }
            bind { lang("extra") }
        }.lparams(matchParent, wrapContent) {
            margin = dip(8)
        }*/
        horizontalScrollView {
            isHorizontalScrollBarEnabled = false
            textView {
                isSelectable = true
                bind { block("block") }
            }
        }.lparams(matchParent, wrapContent) {
            margin = dip(8)
        }
    }
}

class TextBlockView : ItemViewBinderComponent {
    override fun builder(): AnkoContext<*>.() -> Unit = {
        textView {
            isSelectable = true
            bind { text("content") }
        }.lparams(matchParent, wrapContent) {
            margin = dip(8)
        }
    }
}