package com.rayfantasy.icode.ui.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.balysv.materialmenu.MaterialMenuDrawable
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator
import com.rayfantasy.icode.R
import com.rayfantasy.icode.databinding.ActivityBlocksBinding
import com.rayfantasy.icode.extra.PreloadLinearLayoutManager
import com.rayfantasy.icode.model.ICodeTheme
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.CodeGood
import com.rayfantasy.icode.postutil.extension.fromJson
import com.rayfantasy.icode.ui.adapter.BlockAdapter
import kotlinx.android.synthetic.main.activity_blocks.*
import kotlinx.android.synthetic.main.content_blocks.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class BlocksActivity : ActivityBindingStatus() {
    companion object {
        private const val TRANSFORM_DURATION_BG: Long = 300
        private const val TRANSFORM_DURATION_MENU: Int = (TRANSFORM_DURATION_BG * 1.5).toInt()
    }

    private lateinit var codeGood: CodeGood
    private lateinit var binding: ActivityBlocksBinding
    private val menuDrawable by lazy {
        MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN,
                TRANSFORM_DURATION_MENU)
    }
    private var transformFinished = false
    private var scaleY = 0f
    private var translationY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_blocks)
        binding.theme = ICodeTheme
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        codeGood = intent.getSerializableExtra("codeGood") as CodeGood
        title = codeGood.title
        with(recyclerView) {
            layoutManager = PreloadLinearLayoutManager(this@BlocksActivity)
            itemAnimator = RefactoredDefaultItemAnimator()
        }

        codeGood.loadContentFromCache()

        PostUtil.loadCodeContent(codeGood.id!!,
                {
                    with(codeGood) {
                        content = it
                        save()
                    }
                    if (transformFinished) {
                        if (recyclerView.adapter == null)
                            recyclerView.adapter = BlockAdapter(this, codeGood, PostUtil.gson.fromJson(codeGood.content))
                        else {
                            (recyclerView.adapter as BlockAdapter).blocks = PostUtil.gson.fromJson(codeGood.content)
                            recyclerView.adapter.notifyDataSetChanged()
                        }
                    }
                }, { t, rc ->
            toast("rc = $rc")
            t.printStackTrace()
        })
        block_fab.onClick { startActivity<ReplyActivity>("id" to codeGood.id) }

        toolbar.navigationIcon = menuDrawable
        if (intent.hasExtra("y") && intent.hasExtra("height")) {
            if (intent.getBooleanExtra("arrowAnim", true)) {
                menuDrawable.iconState = MaterialMenuDrawable.IconState.BURGER
                menuDrawable.animateIconState(MaterialMenuDrawable.IconState.ARROW)
            } else {
                menuDrawable.iconState = MaterialMenuDrawable.IconState.ARROW
            }
            recyclerView.alpha = 0f
            recyclerView.post {
                scaleY = (intent.getFloatExtra("height", 0f) / recyclerView.height).toFloat()
                translationY = intent.getFloatExtra("y", 0f) - (recyclerView.height / 2)
                recyclerView.scaleY = scaleY
                recyclerView.translationY = translationY
                recyclerView.alpha = 1f
                recyclerView.animate()
                        .setInterpolator(DecelerateInterpolator())
                        .translationY(0f)
                        .scaleY(1f)
                        .setDuration(TRANSFORM_DURATION_BG)
                        .start()
            }
        } else {
            menuDrawable.iconState = MaterialMenuDrawable.IconState.ARROW
        }

        recyclerView.postDelayed({
            codeGood.content?.let { recyclerView.adapter = BlockAdapter(this, codeGood, PostUtil.gson.fromJson(codeGood.content)) }
        }, TRANSFORM_DURATION_BG)
    }

    override fun onBackPressed() {
        if (intent.hasExtra("y") && intent.hasExtra("height")) {
            if (intent.getBooleanExtra("arrowAnim", true)) {
                menuDrawable.animateIconState(MaterialMenuDrawable.IconState.BURGER)
            }
            recyclerView.adapter = null
            recyclerView.animate()
                    .setInterpolator(DecelerateInterpolator())
                    .translationY(translationY)
                    .scaleY(scaleY)
                    .setDuration(TRANSFORM_DURATION_BG)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super@BlocksActivity.onBackPressed()
                            overridePendingTransition(0, 0)
                        }
                    })
                    .start()
        } else {
            super.onBackPressed()
        }
    }
}

fun View.startBlockActivity(codeGood: CodeGood, arrowAnim: Boolean = true) {
    val location = intArrayOf(0, 0)
    getLocationOnScreen(location)
    val y = location[1]
    context.startActivity<BlocksActivity>("codeGood" to codeGood,
            "y" to y.toFloat(),
            "height" to height.toFloat(),
            "arrowAnim" to arrowAnim)
    if (context is Activity)
        (context as Activity).overridePendingTransition(0, 0)
}
