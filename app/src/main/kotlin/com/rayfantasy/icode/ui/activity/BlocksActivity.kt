package com.rayfantasy.icode.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator
import com.rayfantasy.icode.R
import com.rayfantasy.icode.postutil.CodeGood
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.ui.adapter.BlockAdapter
import kotlinx.android.synthetic.main.content_blocks.*
import org.jetbrains.anko.toast

class BlocksActivity : ActivityBase() {
    private lateinit var codeGood: CodeGood

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blocks)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        codeGood = intent.getSerializableExtra("codeGood") as CodeGood

        with(recyclerView) {
            layoutManager = LinearLayoutManager(this@BlocksActivity)
            itemAnimator = RefactoredDefaultItemAnimator()
        }
        PostUtil.loadCodeContent(codeGood.id!!,
                {
                    codeGood.content = it
                    recyclerView.adapter = BlockAdapter(codeGood.blocks)
                }, { t, rc ->
            toast("rc = $rc")
            t.printStackTrace()
        })
    }

}
