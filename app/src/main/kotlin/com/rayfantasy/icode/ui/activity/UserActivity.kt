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
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
    }
    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        user_recyclerview.layoutManager = layoutManager
        adapter = UserListAdapter(this, ArrayList()){}
        user_recyclerview.adapter = adapter
    }

}
