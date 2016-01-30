package com.rayfantasy.icode.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.android.volley.Request
import com.rayfantasy.icode.R
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.ui.adapter.CodeListAdapter
import com.rayfantasy.icode.ui.adapter.LoadMoreAdapter
import kotlinx.android.synthetic.main.activity_account_code.*
import kotlinx.android.synthetic.main.content_account_code.*
import org.jetbrains.anko.support.v4.onRefresh
import java.util.*

class AccountCodeActivity : ActivityBase() {
    private lateinit var adapter: CodeListAdapter
    private var isRefreshing: Boolean = false
    private lateinit var request: Request<out Any>
    private val username = PostUtil.user?.username

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_code)
        swipe_account_layout.onRefresh { refresh() }
        initRecyclerView()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setLogo(R.mipmap.ic_launcher)
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = CodeListAdapter(this@AccountCodeActivity, ArrayList()) {}
        recyclerView.adapter = adapter
        refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        //回收资源
        PostUtil.cancel(request)
        isRefreshing = false
    }

    private fun refresh() {
        //如果正在刷新，则不再发起新的刷新请求
        if (isRefreshing)
            return
        isRefreshing = true
        //按照username查找
        request = PostUtil.selectCodeGood("WHERE username = '$username'", {
            swipe_account_layout.isRefreshing = false
            isRefreshing = false
            if (it.isEmpty()) {
                adapter.footerState = LoadMoreAdapter.FOOTER_STATE_NO_MORE
            } else {
                adapter.codeGoods = it
                adapter.notifyDataSetChanged()
            }
        }, { t, rc ->
            swipe_account_layout.isRefreshing = false
            isRefreshing = false
            t.printStackTrace()
        }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
