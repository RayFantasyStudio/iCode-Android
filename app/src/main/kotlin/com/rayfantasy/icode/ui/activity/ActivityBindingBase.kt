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

import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.benny.library.kbinding.bind.BindingDelegate
import com.benny.library.kbinding.bind.BindingDisposer
import com.benny.library.kbinding.view.BindingDisposerGenerator
import com.benny.library.kbinding.viewmodel.ViewModel

open class ActivityBindingBase : AppCompatActivity(), BindingDisposerGenerator, BindingDelegate {
    override val bindingDisposer = BindingDisposer()
    override val viewModel = ViewModel()

    override fun onDestroy() {
        super.onDestroy()
        bindingDisposer.unbind()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed(); true
        }
        else -> super.onOptionsItemSelected(item)
    }
}