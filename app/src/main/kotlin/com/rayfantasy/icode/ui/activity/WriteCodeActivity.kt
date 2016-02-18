package com.rayfantasy.icode.ui.activity

import android.app.Activity
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.android.volley.Request
import com.balysv.materialmenu.MaterialMenuDrawable
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.nineoldandroids.animation.ArgbEvaluator
import com.nineoldandroids.animation.ValueAnimator
import com.rayfantasy.icode.R
import com.rayfantasy.icode.databinding.ActivityWriteCodeBinding
import com.rayfantasy.icode.extension.addListener
import com.rayfantasy.icode.extension.snackBar
import com.rayfantasy.icode.extra.PreloadLinearLayoutManager
import com.rayfantasy.icode.model.ICodeTheme
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.CodeGood
import com.rayfantasy.icode.postutil.extension.e
import com.rayfantasy.icode.ui.adapter.EditBlockAdapter
import com.rayfantasy.icode.util.error
import io.codetail.animation.SupportAnimator
import io.codetail.animation.ViewAnimationUtils
import kotlinx.android.synthetic.main.activity_write_code.*
import kotlinx.android.synthetic.main.content_write_code.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity

class WriteCodeActivity : ActivityBase() {
    companion object {
        private const val TRANSFORM_DURATION_BG: Long = 300
        private const val TRANSFORM_DURATION_MENU: Int = (TRANSFORM_DURATION_BG * 1.5).toInt()
    }

    private val menuDrawable by lazy {
        MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN,
                TRANSFORM_DURATION_MENU)
    }
    private lateinit var supportAnimator: SupportAnimator
    private var backPressed = false
    private var request: Request<*>? = null

    override val bindingStatus: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityWriteCodeBinding>(this, R.layout.activity_write_code).theme = ICodeTheme
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val blockAdapter = EditBlockAdapter(this)
        val recyclerViewDragDropManager = RecyclerViewDragDropManager()
        recyclerViewDragDropManager.setInitiateOnLongPress(true)
        recyclerViewDragDropManager.setInitiateOnMove(false)
        with(recyclerView) {
            layoutManager = PreloadLinearLayoutManager(this@WriteCodeActivity)
            itemAnimator = RefactoredDefaultItemAnimator()
        }
        write_code_tv_fab.onClick {
            if (request != null) return@onClick

            request = PostUtil.insertCodeGood(CodeGood(
                    blockAdapter.title,
                    blockAdapter.subTitle,
                    blockAdapter.content),
                    {
                        write_code_tv_fab.snackBar(getText(R.string.upload_success), Snackbar.LENGTH_LONG)
                        finish()
                        request = null
                    },
                    { t, rc ->
                        e("failed, rc =  $rc")
                        write_code_tv_fab.snackBar("${getText(R.string.cannot_upload)}${error("insertCodeGood", rc, this)}", Snackbar.LENGTH_LONG)
                        request = null
                    })
        }

        toolbar.navigationIcon = menuDrawable
        menuDrawable.iconState = MaterialMenuDrawable.IconState.BURGER
        menuDrawable.animateIconState(MaterialMenuDrawable.IconState.ARROW)
        recyclerView.post {
            val cx = intent.getIntExtra("x", 0)
            val cy = intent.getIntExtra("y", 0) - toolbar.height
            val dx = Math.max(cx, recyclerView.width - cx).toDouble()
            val dy = Math.max(cy, recyclerView.height - cy).toDouble()
            val finalRadius = Math.hypot(dx, dy).toFloat()
            supportAnimator = ViewAnimationUtils.createCircularReveal(recyclerView, cx, cy,
                    intent.getIntExtra("height", 0) / 2f, finalRadius)
            supportAnimator.interpolator = AccelerateDecelerateInterpolator()
            supportAnimator.duration = TRANSFORM_DURATION_BG
            supportAnimator.start()
            val valueAnim = ValueAnimator.ofObject(ArgbEvaluator(), ICodeTheme.colorAccent.get(),
                    resources.getColor(R.color.background_material_light))
            valueAnim.addUpdateListener {
                recyclerView.backgroundColor = it.animatedValue as Int
            }
            valueAnim.duration = TRANSFORM_DURATION_BG
            valueAnim.addListener {
                onAnimationEnd {
                    if (!backPressed) {
                        recyclerView.adapter = recyclerViewDragDropManager.createWrappedAdapter(blockAdapter)
                        recyclerViewDragDropManager.attachRecyclerView(recyclerView)
                    }
                }
            }
            valueAnim.start()
        }
    }

    override fun onBackPressed() {
        if (backPressed) return
        backPressed = true
        menuDrawable.animateIconState(MaterialMenuDrawable.IconState.BURGER)
        recyclerView.adapter = null
        supportAnimator.cancel()
        revealLayout.post { supportAnimator.reverse().start() }
        val valueAnim = ValueAnimator.ofObject(ArgbEvaluator(),
                resources.getColor(R.color.background_material_light), ICodeTheme.colorAccent.get())
        valueAnim.addUpdateListener {
            recyclerView.backgroundColor = it.animatedValue as Int
        }
        valueAnim.duration = TRANSFORM_DURATION_BG
        valueAnim.addListener {
            onAnimationEnd {
                recyclerView.visibility = View.INVISIBLE
                super.onBackPressed()
                overridePendingTransition(0, 0)
            }
        }
        valueAnim.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        request?.let {
            PostUtil.cancel(request)
            request = null
        }
    }
}

fun FloatingActionButton.startWriteCodeActivity() {
    val location = intArrayOf(0, 0)
    getLocationOnScreen(location)
    val x = location[0]
    val y = location[1]
    context.startActivity<WriteCodeActivity>(
            "y" to y,
            "x" to x + (width shr 1),
            "height" to height)
    if (context is Activity)
        (context as Activity).overridePendingTransition(0, 0)
}
