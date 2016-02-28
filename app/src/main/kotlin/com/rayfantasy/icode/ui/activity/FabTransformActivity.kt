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

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import com.balysv.materialmenu.MaterialMenuDrawable
import com.rayfantasy.icode.R
import com.rayfantasy.icode.extension.colorAnim
import com.rayfantasy.icode.model.ICodeTheme
import io.codetail.animation.SupportAnimator
import io.codetail.animation.ViewAnimationUtils
import org.jetbrains.anko.backgroundColor
import java.io.Serializable

abstract class FabTransformActivity : ActivityBase() {
    companion object {
        private const val TRANSFORM_DURATION_BG: Long = 300
        private const val TRANSFORM_DURATION_MENU: Int = (TRANSFORM_DURATION_BG * 1.5).toInt()
    }

    protected abstract val revealLayout: ViewGroup
    protected open val arrowAnim = true
    private val childView by lazy { revealLayout.getChildAt(0) }

    private val menuDrawable by lazy {
        MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN,
                TRANSFORM_DURATION_MENU)
    }
    private lateinit var supportAnimator: SupportAnimator
    protected var backPressed = false
        private set
    protected var transformFinished = false
        private set
    protected open var onStartAnimEnd: (() -> Unit)? = null


    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.navigationIcon = menuDrawable
        if (arrowAnim) {
            menuDrawable.iconState = MaterialMenuDrawable.IconState.BURGER
            menuDrawable.animateIconState(MaterialMenuDrawable.IconState.ARROW)
        } else
            menuDrawable.iconState = MaterialMenuDrawable.IconState.ARROW
        revealLayout.post {
            val cx = intent.getIntExtra("x", 0)
            val cy = intent.getIntExtra("y", 0) - toolbar.height
            val dx = Math.max(cx, revealLayout.width - cx).toDouble()
            val dy = Math.max(cy, revealLayout.height - cy).toDouble()
            val finalRadius = Math.hypot(dx, dy).toFloat()
            supportAnimator = ViewAnimationUtils.createCircularReveal(childView, cx, cy,
                    intent.getIntExtra("height", 0) / 2f, finalRadius)
            supportAnimator.interpolator = AccelerateDecelerateInterpolator()
            supportAnimator.duration = TRANSFORM_DURATION_BG
            supportAnimator.start()
            colorAnim(ICodeTheme.colorAccent.get(), resources.getColor(R.color.activity_background),
                    TRANSFORM_DURATION_BG, { childView.backgroundColor = it }, {
                onAnimationEnd {
                    if (!backPressed) {
                        onStartAnimEnd?.invoke()
                        transformFinished = true
                    }
                }
            })
        }
    }

    override fun onBackPressed() {
        if (backPressed) return
        backPressed = true
        if (arrowAnim)
            menuDrawable.animateIconState(MaterialMenuDrawable.IconState.BURGER)
        supportAnimator.cancel()
        revealLayout.post {
            supportAnimator.reverse().start()
        }
        colorAnim(resources.getColor(R.color.activity_background), ICodeTheme.colorAccent.get(),
                TRANSFORM_DURATION_BG, { childView.backgroundColor = it }, {
            onAnimationEnd {
                revealLayout.visibility = View.INVISIBLE
                super.onBackPressed()
                overridePendingTransition(0, 0)
            }
        })
    }
}

inline fun <reified T : FabTransformActivity> FloatingActionButton.startFabTransformActivity(vararg params: Pair<String, Serializable>) {
    val location = intArrayOf(0, 0)
    getLocationOnScreen(location)
    val x = location[0]
    val y = location[1]
    val mutableList = params.toMutableList()
    mutableList.addAll(arrayOf(
            "y" to y,
            "x" to x + (width shr 1),
            "height" to height))
    val intent = Intent(context, T::class.java)
    mutableList.forEach { intent.putExtra(it.first, it.second) }
    context.startActivity(intent)
    if (context is Activity)
        (context as Activity).overridePendingTransition(0, 0)
}