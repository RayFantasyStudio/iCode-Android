package com.rayfantasy.icode.ui.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.ViewGroup
import com.android.volley.Request
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.rayfantasy.icode.R
import com.rayfantasy.icode.databinding.ActivityWriteCodeBinding
import com.rayfantasy.icode.extension.snackBar
import com.rayfantasy.icode.extra.PreloadLinearLayoutManager
import com.rayfantasy.icode.model.ICodeTheme
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.CodeGood
import com.rayfantasy.icode.postutil.extension.e
import com.rayfantasy.icode.ui.adapter.EditBlockAdapter
import com.rayfantasy.icode.util.error
import io.codetail.animation.SupportAnimator
import kotlinx.android.synthetic.main.activity_write_code.*
import kotlinx.android.synthetic.main.content_write_code.*
import org.jetbrains.anko.onClick

class WriteCodeActivity : FabTransformActivity() {
    private lateinit var supportAnimator: SupportAnimator
    private var backPressed = false
    private var request: Request<*>? = null
    override val revealLayout: ViewGroup
        get() = reveal_layout

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

        onStartAnimEnd = {
            recyclerView.adapter = recyclerViewDragDropManager.createWrappedAdapter(blockAdapter)
            recyclerViewDragDropManager.attachRecyclerView(recyclerView)
        }
    }

    override fun onBackPressed() {
        recyclerView.adapter = null
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        request?.let {
            PostUtil.cancel(request)
            request = null
        }
    }
}
