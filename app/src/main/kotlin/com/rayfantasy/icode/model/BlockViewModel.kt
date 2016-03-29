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

package com.rayfantasy.icode.model

import com.benny.library.kbinding.adapterview.viewmodel.ItemViewModel
import com.benny.library.kbinding.annotation.ExtractProperty
import com.rayfantasy.icode.postutil.bean.CodeGood
import kotlin.properties.Delegates

class BlockViewModel : ItemViewModel<CodeGood.Block>() {
    @delegate:ExtractProperty("content", "extra", "blockType", hasPrefix = false)
    var block by Delegates.property<CodeGood.Block>()

    /*@delegate:DependencyProperty("block")
    val content by Delegates.property { block!!.content }

    @delegate:DependencyProperty("block")
    val extra by Delegates.property { block!!.extra }

    @delegate:DependencyProperty("block")
    val blockType by Delegates.property { block!!.blockType }*/

    override fun onDataChange(data: CodeGood.Block?) {
        block = data
    }
}