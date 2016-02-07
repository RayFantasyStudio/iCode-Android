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
import com.rayfantasy.icode.R
import org.jetbrains.anko.defaultSharedPreferences

object ICodeTheme {
    const val PREF_ICODE_THEME = "pref_icode_theme"
    const val THEME_BLUE = 0
    const val THEME_RED = 1
    const val THEME_DEFAULT = THEME_BLUE
    internal val colorPrimaryRes = intArrayOf(R.color.colorPrimary_blue, R.color.colorPrimary_red)
    internal val colorPrimaryDarkRes = intArrayOf(R.color.colorPrimaryDark_blue, R.color.colorPrimaryDark_red)
    internal val colorAccentRes = intArrayOf(R.color.colorAccent_blue, R.color.colorAccent_red)

    fun init(ctx: Context) {
        val theme = ctx.defaultSharedPreferences.getInt(PREF_ICODE_THEME, THEME_DEFAULT)
        ctx.changeTheme(theme)
    }

    val colorPrimary = ObservableInt()

    val colorPrimaryDark = ObservableInt()

    val colorAccent = ObservableInt()

    val icon = ObservableInt()
}

fun Context.changeTheme(theme: Int) = with(ICodeTheme) {
    colorPrimary.set(resources.getColor(colorPrimaryRes[theme]))
    colorPrimaryDark.set(resources.getColor(colorPrimaryDarkRes[theme]))
    colorAccent.set(resources.getColor(colorAccentRes[theme]))
    defaultSharedPreferences.edit().putInt(PREF_ICODE_THEME, theme).apply()
}