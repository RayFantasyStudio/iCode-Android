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
import com.benny.library.kbinding.annotation.Command
import com.benny.library.kbinding.annotation.Property
import com.benny.library.kbinding.view.setContentView
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.CodeGood
import com.rayfantasy.icode.postutil.extension.fromJson
import com.rayfantasy.icode.postutil.extension.loadCodeContent
import com.rayfantasy.icode.ui.fragment.SettingFragment
import com.rayfantasy.icode.ui.layout.CodeDetailActivityUI
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.toast
import kotlin.properties.Delegates

class CodeDetailActivity : ActivityBindingBase() {
    override val bindingStatus: Boolean
        get() = true

    @delegate:Property
    var blocks: List<CodeGood.Block>? by Delegates.property()

    private val codeGood by lazy { intent.getSerializableExtra("codeGood") as CodeGood }
    val highlightTheme by lazy {
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
    fun itemClick(params: Int, canExecute: (Boolean) -> Unit) {
    }

}