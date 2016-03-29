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

import android.view.ViewManager
import com.benny.library.kbinding.bind.BindingMode
import com.benny.library.kbinding.bind.OneWay
import com.benny.library.kbinding.bind.oneWayPropertyBinding
import com.benny.library.kbinding.converter.EmptyOneWayConverter1
import com.benny.library.kbinding.converter.OneWayConverter
import org.evilbinary.highliter.HighlightEditText
import org.evilbinary.managers.Configure
import org.jetbrains.anko.custom.ankoView
import rx.functions.Action1

fun ViewManager.highlightEditText(theme: String, init: HighlightEditText.() -> Unit) = ankoView({
    val configure = Configure(it)
    configure.mTheme = theme
    HighlightEditText(it, configure)
}, init)

fun HighlightEditText.source(vararg paths: String, mode: OneWay = BindingMode.OneWay,
                             converter: OneWayConverter<*, String> = EmptyOneWayConverter1()) = oneWayPropertyBinding(paths, source(this), false, converter)

private fun source(het: HighlightEditText) = Action1<String> { het.setSource(it) }

fun HighlightEditText.lang(vararg paths: String, mode: OneWay = BindingMode.OneWay,
                           converter: OneWayConverter<*, String> = EmptyOneWayConverter1()) = oneWayPropertyBinding(paths, lang(this), false, converter)

private fun lang(het: HighlightEditText) = Action1<String> {
    val conf = het.configure
    conf.mLanguage = it
    het.loadFromConfigure(conf)
}