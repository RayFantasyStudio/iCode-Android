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

import android.content.Context
import android.databinding.ObservableInt
import com.rayfantasy.icode.extension.colorAnim
import com.rayfantasy.icode.theme.ThemeModel.PREF_ICODE_THEME
import com.rayfantasy.icode.theme.ThemeModel.THEME_DEFAULT
import com.rayfantasy.icode.theme.ThemeModel.colorAccentRes
import com.rayfantasy.icode.theme.ThemeModel.colorHighLightRes
import com.rayfantasy.icode.theme.ThemeModel.colorPrimaryDarkRes
import com.rayfantasy.icode.theme.ThemeModel.colorPrimaryRes
import org.jetbrains.anko.defaultSharedPreferences

object ICodeTheme {
    fun init(ctx: Context) {
        val theme = ctx.defaultSharedPreferences.getInt(PREF_ICODE_THEME, THEME_DEFAULT)
        changeTheme(ctx, theme)
    }

    val colorPrimary = ObservableInt()

    val colorPrimaryDark = ObservableInt()

    val colorAccent = ObservableInt()

    val colorHighLight = ObservableInt()

    val icon = ObservableInt()

    fun changeTheme(ctx: Context, theme: Int) = with(ICodeTheme) {
        with(ctx) {
            changeColor(colorPrimary, resources.getColor(colorPrimaryRes[theme]))
            changeColor(colorPrimaryDark, resources.getColor(colorPrimaryDarkRes[theme]))
            changeColor(colorAccent, resources.getColor(colorAccentRes[theme]))
            changeColor(colorHighLight, resources.getColor(colorHighLightRes[theme]))
        }
    }

    private fun changeColor(observableInt: ObservableInt, color: Int) {
        val i = observableInt.get()
        if (i == color) return
        if (i != 0) {
            colorAnim(i, color, 300, { observableInt.set(it) })
        } else
            observableInt.set(color)
    }
}
