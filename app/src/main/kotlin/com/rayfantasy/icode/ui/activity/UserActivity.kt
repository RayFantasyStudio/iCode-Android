package com.rayfantasy.icode.ui.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.android.volley.Request
import com.rayfantasy.icode.R
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.ui.adapter.UserListAdapter
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.content_user.*
import java.util.*

class UserActivity : AppCompatActivity() {
    private lateinit var adapter : UserListAdapter
    private var isRefreshing: Boolean = false
    private lateinit var request: Request<out Any>
    private val username = PostUtil.user?.username
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(toolbar)
        initRecyclerView()
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }

    }
    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        user_recyclerview.layoutManager = layoutManager
        adapter = UserListAdapter(this, ArrayList()) {}
        user_recyclerview.adapter = adapter
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
            isRefreshing = false
            if (it.isEmpty()) {
                adapter.setFooterState(UserListAdapter.FOOTER_STATE_NO_MORE)
            }

            //重复利用原来的adapter，节省内存
            if (adapter == null) {
                adapter = UserListAdapter(this, it) {}
                user_recyclerview.adapter = adapter
            } else {
                adapter!!.codeGoods = it
                adapter!!.notifyDataSetChanged()
            }
        }, { t, rc ->
            isRefreshing = false
            t.printStackTrace()
        }
        )
    }
}
