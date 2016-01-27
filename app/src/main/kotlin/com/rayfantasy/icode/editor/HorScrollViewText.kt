/*
 * Copyright 2015. Alex Zhang aka. ztc1997
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rayfantasy.icode.editor

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.ViewManager
import android.widget.HorizontalScrollView

import com.rayfantasy.icode.R
import org.jetbrains.anko.custom.ankoView


class HorScrollViewText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : HorizontalScrollView(context, attrs) {


    var containerView: CodeEditor? = null


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    override fun computeScroll() {
        super.computeScroll()

        containerView?.scrollX(scrollX)
        containerView?.getViewWidth(width)
    }


    override fun onTouchEvent(ev: MotionEvent): Boolean {

        return super.onTouchEvent(ev)
    }

    override fun executeKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
            scrollTo(0, scrollY)
        }


        return super.executeKeyEvent(event)
    }


    val rowHeight: Int
        get() {
            if (containerView == null) {
                containerView = findViewById(R.id.codeEditor) as CodeEditor
            }
            return containerView!!.rowHeight
        }

    val totalRows: Int
        get() = containerView!!.totalRows

    val currRow: Int
        get() = containerView!!.currRow

}

@Suppress("NOTHING_TO_INLINE")
public inline fun ViewManager.horScrollViewText() = horScrollViewText {}

public inline fun ViewManager.horScrollViewText(init: HorScrollViewText.() -> Unit) = ankoView({ HorScrollViewText(it) }, init)
