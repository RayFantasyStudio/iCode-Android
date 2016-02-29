package com.rayfantasy.icode.ui.activity

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.balysv.materialmenu.MaterialMenuDrawable
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.rayfantasy.icode.R
import com.rayfantasy.icode.databinding.ActivityBlocksBinding
import com.rayfantasy.icode.extension.colorAnim
import com.rayfantasy.icode.extension.onAnimationEnd
import com.rayfantasy.icode.extension.setListener
import com.rayfantasy.icode.extension.snackBar
import com.rayfantasy.icode.extra.PreloadLinearLayoutManager
import com.rayfantasy.icode.model.ICodeTheme
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.CodeGood
import com.rayfantasy.icode.postutil.bean.CodeGood_Table
import com.rayfantasy.icode.postutil.bean.Favorite
import com.rayfantasy.icode.postutil.extension.fromJson
import com.rayfantasy.icode.ui.adapter.BlockAdapter
import kotlinx.android.synthetic.main.activity_blocks.*
import kotlinx.android.synthetic.main.content_blocks.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.onClick
import org.jetbrains.anko.recyclerview.v7.onScrollListener
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class BlocksActivity : ActivityBase() {
    companion object {
        private const val TRANSFORM_DURATION_BG: Long = 300
        private const val TRANSFORM_DURATION_MENU: Int = (TRANSFORM_DURATION_BG * 1.5).toInt()
    }

    private lateinit var codeGood: CodeGood
    private lateinit var broadcastManager: LocalBroadcastManager
    private lateinit var binding: ActivityBlocksBinding
    private val menuDrawable by lazy {
        MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN,
                TRANSFORM_DURATION_MENU)
    }
    private var transformFinished = false
    private var backPressed = false
    private var scaleY = 0f
    private var translationY = 0f

    override val bindingStatus: Boolean
        get() = true

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

        PostUtil.loadCodeContent(codeGood.id!!) {
            onSuccess {
                with(codeGood) {
                    content = it
                    save()
                }
                if (transformFinished) {
                    if (recyclerView.adapter == null)
                        recyclerView.adapter = BlockAdapter(this@BlocksActivity, codeGood, PostUtil.gson.fromJson(codeGood.content))
                    else {
                        (recyclerView.adapter as BlockAdapter).blocks = PostUtil.gson.fromJson(codeGood.content)
                        recyclerView.adapter.notifyDataSetChanged()
                    }
                }
            }
            onFailed { throwable, rc ->
                toast("rc = $rc")
                throwable.printStackTrace()
            }

        }

        fab.onClick { fab.startFabTransformActivity<ReplyActivity>("id" to codeGood.id, "reply_count" to codeGood.reply) }

        toolbar.navigationIcon = menuDrawable
        if (intent.hasExtra("y") && intent.hasExtra("height")) {
            if (intent.getBooleanExtra("arrowAnim", true)) {
                menuDrawable.iconState = MaterialMenuDrawable.IconState.BURGER
                menuDrawable.animateIconState(MaterialMenuDrawable.IconState.ARROW)
            } else {
                menuDrawable.iconState = MaterialMenuDrawable.IconState.ARROW
                with(fab) {
                    alpha = 0f
                    post {
                        toggle(false, 0)
                        alpha = 1f
                        toggle(true, TRANSFORM_DURATION_BG)
                    }
                }
            }

            recyclerView.post {
                val height = intent.getIntExtra("height", 0)
                scaleY = height / recyclerView.height.toFloat()
                translationY = (intent.getIntExtra("y", 0) - (recyclerView.height ushr 1) + (height ushr 1) - toolbar.minimumHeight).toFloat()
                recyclerView.scaleY = scaleY
                recyclerView.translationY = translationY
                recyclerView.animate()
                        .setInterpolator(DecelerateInterpolator())
                        .translationY(0f)
                        .scaleY(1f)
                        .setDuration(TRANSFORM_DURATION_BG)
                        .setListener {
                            onAnimationStart { recyclerView.postDelayed({ recyclerView.visibility = View.VISIBLE }, 10) }
                            onAnimationEnd {
                                if (!backPressed) {
                                    codeGood.content?.let {
                                        recyclerView.adapter = BlockAdapter(this@BlocksActivity, codeGood, PostUtil.gson.fromJson(codeGood.content))
                                    }
                                    transformFinished = true
                                }
                            }
                        }
                        .start()
                if (codeGood.highlight)
                    colorAnim(ICodeTheme.colorHighLight.get(),
                            resources.getColor(R.color.activity_background), TRANSFORM_DURATION_BG, {
                        if (!backPressed)
                            recyclerView.backgroundColor = it
                    })
            }
        } else {
            menuDrawable.iconState = MaterialMenuDrawable.IconState.ARROW
        }

        recyclerView.onScrollListener {
            onScrolled { recyclerView, dx, dy ->
                if (Math.abs(dy) > 20)
                    fab.toggle(dy < 0, 100)
            }
        }
    }

    override fun onBackPressed() {
        if (backPressed) return
        backPressed = true
        if (intent.hasExtra("y") && intent.hasExtra("height")) {
            if (intent.getBooleanExtra("arrowAnim", true)) {
                menuDrawable.animateIconState(MaterialMenuDrawable.IconState.BURGER)
            } else {
                fab.toggle(false, TRANSFORM_DURATION_BG)
            }
            recyclerView.adapter = null
            recyclerView.animate()
                    .setInterpolator(DecelerateInterpolator())
                    .translationY(translationY)
                    .scaleY(scaleY)
                    .setDuration(TRANSFORM_DURATION_BG)
                    .onAnimationEnd {
                        recyclerView.visibility = View.INVISIBLE
                        super.onBackPressed()
                        overridePendingTransition(0, 0)
                    }
                    .start()
            if (codeGood.highlight)
                colorAnim(resources.getColor(R.color.activity_background),
                        ICodeTheme.colorHighLight.get(), TRANSFORM_DURATION_BG, {
                    recyclerView.backgroundColor = it
                })
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (PostUtil.user == null) return false
        if (codeGood.username.equals(PostUtil.user!!.username)) {
            menuInflater.inflate(R.menu.block_menu, menu)
            return true
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_edit -> {
                delCodeGood()
                return true
            }
            else -> return false
        }
    }

    fun delCodeGood() {

        PostUtil.delCodeGood(codeGood.id) {
            onSuccess {
                fab.snackBar("删除成功", Snackbar.LENGTH_SHORT)
                SQLite.delete(CodeGood::class.java).where(CodeGood_Table.id.`is`(codeGood.id))
                SQLite.delete(Favorite::class.java).where(CodeGood_Table.id.`is`(codeGood.id))
                val intent: Intent = Intent(this@BlocksActivity, MainActivity::class.java)
                intent.putExtra("code_id", codeGood.id)
                broadcastManager.sendBroadcast(intent)
                finish()
            }
            onFailed { t, rc -> fab.snackBar("删除失败，错误代码：$rc", Snackbar.LENGTH_SHORT) }

        }
    }

}

fun View.startBlockActivity(codeGood: CodeGood, arrowAnim: Boolean = true) {
    val location = intArrayOf(0, 0)
    getLocationOnScreen(location)
    val y = location[1]
    context.startActivity<BlocksActivity>("codeGood" to codeGood,
            "y" to y,
            "height" to height.toInt(),
            "arrowAnim" to arrowAnim)
    if (context is Activity)
        (context as Activity).overridePendingTransition(0, 0)
}
