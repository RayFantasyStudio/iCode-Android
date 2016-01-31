package com.rayfantasy.icode.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator
import com.rayfantasy.icode.R
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.CodeGood
import com.rayfantasy.icode.postutil.extension.fromJson
import com.rayfantasy.icode.ui.adapter.BlockAdapter
import kotlinx.android.synthetic.main.activity_blocks.*
import kotlinx.android.synthetic.main.content_blocks.*
import kotlinx.android.synthetic.main.item_recycler_code_list.*
import org.jetbrains.anko.toast

class BlocksActivity : ActivityBase() {
    private lateinit var codeGood: CodeGood

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blocks)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        codeGood = intent.getSerializableExtra("codeGood") as CodeGood
        title = codeGood.title
        toolbar.subtitle = codeGood.subtitle
        with(recyclerView) {
            layoutManager = LinearLayoutManager(this@BlocksActivity)
            itemAnimator = RefactoredDefaultItemAnimator()
        }

        if (codeGood.content == null)
            PostUtil.loadCodeContent(codeGood.id!!,
                    {
                        with(codeGood) {
                            content = it
                            save()
                        }
                        recyclerView.adapter = BlockAdapter(PostUtil.gson.fromJson(codeGood.content))
                    }, { t, rc ->
                toast("rc = $rc")
                t.printStackTrace()
            })
        else
            recyclerView.adapter = BlockAdapter(PostUtil.gson.fromJson(codeGood.content))
    }

}
