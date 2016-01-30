package com.rayfantasy.icode.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.rayfantasy.icode.R
import com.rayfantasy.icode.postutil.CodeGood
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.extension.e
import com.rayfantasy.icode.ui.adapter.EditBlockAdapter
import kotlinx.android.synthetic.main.activity_write_code.*
import kotlinx.android.synthetic.main.content_write_code.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast

class WriteCodeActivity : ActivityBase() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_code)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val blockAdapter = EditBlockAdapter(this)
        val recyclerViewDragDropManager = RecyclerViewDragDropManager()
        recyclerViewDragDropManager.setInitiateOnLongPress(true)
        recyclerViewDragDropManager.setInitiateOnMove(false)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(this@WriteCodeActivity)
            adapter = recyclerViewDragDropManager.createWrappedAdapter(blockAdapter)
            itemAnimator = RefactoredDefaultItemAnimator()
            recyclerViewDragDropManager.attachRecyclerView(this)
        }
        write_code_tv_fab.onClick {
            PostUtil.insertCodeGood(CodeGood(
                    blockAdapter.title,
                    blockAdapter.subTitle,
                    blockAdapter.content,
                    PostUtil.user!!.username),
                    {
                        toast("Success")
                        finish()
                    },
                    { t, rc ->
                        e("failed, rc =  $rc")
                        longToast("发布失败, rc =  $rc")
                    })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> false
    }
}


