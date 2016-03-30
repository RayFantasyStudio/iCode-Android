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

import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.view.ContextThemeWrapper
import android.util.TypedValue
import android.view.View
import android.view.ViewManager
import android.widget.LinearLayout
import com.rayfantasy.icode.R
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7._Toolbar
import org.jetbrains.anko.appcompat.v7.`$$Anko$Factories$AppcompatV7ViewGroup`
import org.jetbrains.anko.design._AppBarLayout
import org.jetbrains.anko.design._CoordinatorLayout
import org.jetbrains.anko.design.`$$Anko$Factories$DesignViewGroup`
import org.jetbrains.anko.internals.AnkoInternals

fun collapseModePin(): CollapsingToolbarLayout.LayoutParams.() -> Unit = { collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN }

fun Context.snackBar(view: View, text: CharSequence, length: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit) = Snackbar.make(view, text, length).apply { init() }.show()
fun View.snackBar(text: CharSequence, length: Int = Snackbar.LENGTH_SHORT, snackbar: Snackbar.() -> Unit) = context.snackBar(this, text, length, snackbar)
fun Fragment.snackBar(view: View, text: CharSequence, length: Int = Snackbar.LENGTH_SHORT, snackbar: Snackbar.() -> Unit) = context.snackBar(view, text, length, snackbar)

fun Context.attr(@AttrRes attribute: Int): TypedValue {
    var typed = TypedValue()
    ctx.theme.resolveAttribute(attribute, typed, true)
    return typed
}

//returns px
fun Context.dimenAttr(@AttrRes attribute: Int): Int = TypedValue.complexToDimensionPixelSize(attr(attribute).data, resources.displayMetrics)

//returns color
fun Context.colorAttr(@AttrRes attribute: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(attr(attribute).resourceId, ctx.theme)
    } else {
        @Suppress("DEPRECATION")
        resources.getColor(attr(attribute).resourceId)
    }
}

fun AnkoContext<*>.dimenAttr(@AttrRes attribute: Int): Int = ctx.dimenAttr(attribute)
fun AnkoContext<*>.colorAttr(@AttrRes attribute: Int): Int = ctx.colorAttr(attribute)
fun AnkoContext<*>.attribute(@AttrRes attribute: Int): TypedValue = ctx.attr(attribute)

fun View.dimenAttr(@AttrRes attribute: Int): Int = context.dimenAttr(attribute)
fun View.colorAttr(@AttrRes attribute: Int): Int = context.colorAttr(attribute)
fun View.attr(@AttrRes attribute: Int): TypedValue = context.attr(attribute)

fun Fragment.dimenAttr(@AttrRes attribute: Int): Int = activity.dimenAttr(attribute)
fun Fragment.colorAttr(@AttrRes attribute: Int): Int = activity.colorAttr(attribute)
fun Fragment.attr(@AttrRes attribute: Int): TypedValue = activity.attr(attribute)

fun ViewManager.appBarLayout(@StyleRes theme: Int): AppBarLayout = appBarLayout(theme, {})
inline fun ViewManager.appBarLayout(@StyleRes theme: Int, init: _AppBarLayout.() -> Unit): AppBarLayout {
    return ankoView(theme, `$$Anko$Factories$DesignViewGroup`.APP_BAR_LAYOUT) { init() }
}

fun ViewManager.toolbar(@StyleRes theme: Int): _Toolbar = toolbar(theme, {})
inline fun ViewManager.toolbar(@StyleRes theme: Int, init: _Toolbar.() -> Unit): _Toolbar {
    return ankoView(theme, `$$Anko$Factories$AppcompatV7ViewGroup`.TOOLBAR) { init() }
}

fun ViewManager.linearLayout(@StyleRes theme: Int): LinearLayout = linearLayout(theme, {})
inline fun ViewManager.linearLayout(@StyleRes theme: Int, init: _LinearLayout.() -> Unit): LinearLayout {
    return ankoView(theme, `$$Anko$Factories$Sdk15ViewGroup`.LINEAR_LAYOUT) { init() }
}

fun ViewManager.coordinatorLayout(@StyleRes theme: Int) = coordinatorLayout(theme, {})
inline fun ViewManager.coordinatorLayout(@StyleRes theme: Int, init: _CoordinatorLayout.() -> Unit): _CoordinatorLayout {
    return ankoView(theme, `$$Anko$Factories$DesignViewGroup`.COORDINATOR_LAYOUT, init)
}

fun ViewManager.fitedCoordinatorLayout(init: _CoordinatorLayout.() -> Unit) = coordinatorLayout(R.style.FitedStyle, init)

inline fun <T : View> ViewManager.ankoView(@StyleRes theme: Int, factory: (ctx: Context) -> T, init: T.() -> Unit): T {
    var ctx = AnkoInternals.getContext(this)
    if (theme != 0 && (ctx !is ContextThemeWrapper || ctx.themeResId != theme)) {
        // If the context isn't a ContextThemeWrapper, or it is but does not have
        // the same theme as we need, wrap it in a new wrapper
        ctx = ContextThemeWrapper(ctx, theme)
    }

    val view = factory(ctx)
    view.init()
    AnkoInternals.addView(this, view)
    return view
}

private val defaultInit: Any.() -> Unit = {}
fun <T : View> T.lparams(
        width: Int = wrapContent, height: Int = wrapContent,
        init: CollapsingToolbarLayout.LayoutParams.() -> Unit = defaultInit): T {
    val layoutParams = CollapsingToolbarLayout.LayoutParams(width, height)
    layoutParams.init()
    this@lparams.layoutParams = layoutParams
    return this
}
