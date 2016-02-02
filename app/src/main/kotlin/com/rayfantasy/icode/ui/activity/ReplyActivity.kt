package com.rayfantasy.icode.ui.activity

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.android.volley.Request
import com.raizlabs.android.dbflow.sql.language.Select
import com.rayfantasy.icode.postutil.bean.Reply_Table
import com.rayfantasy.icode.R
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.Reply
import com.rayfantasy.icode.ui.adapter.LoadMoreAdapter
import com.rayfantasy.icode.ui.adapter.ReplyListAdapter
import kotlinx.android.synthetic.main.activity_reply.*
import kotlinx.android.synthetic.main.content_reply.*
import org.apache.commons.collections4.list.SetUniqueList
import org.jetbrains.anko.support.v4.onRefresh

class ReplyActivity : AppCompatActivity() {
    private   var id : Int = 1
    private lateinit var adapter: ReplyListAdapter
    private val isRefreshing: Boolean
        get() = request != null
    private var request: Request<*>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        title = "评论"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        id = intent.getSerializableExtra("id") as Int
        reply_swip?.onRefresh {
            loadReplys(false)
        }

        initRecyclerView()

    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        reply_recyclerview.layoutManager = layoutManager
        adapter = ReplyListAdapter(this, SetUniqueList.setUniqueList(getCacheData())) { loadReplys(true) }
        reply_recyclerview.adapter = adapter

    }
    private fun loadReplys(refresh: Boolean) {
        //如果正在刷新，则不再发起新的刷新请求
        if (isRefreshing)
            return

        //生成加载条件，目前加载5个，方便测试

        val condition = "${if (!refresh && adapter.replyList.isNotEmpty()) "WHERE createat < ${adapter.replyList.last().createAt} " else ""}" +
                "ORDER BY createat DESC LIMIT 0, 5"
        request = PostUtil.findReply(condition, {
            reply_swip.isRefreshing = false
            request = null

            if (it.isEmpty() ) {
                //如果结果为空，则表示没有更多内容了
                adapter.footerState = LoadMoreAdapter.FOOTER_STATE_NO_MORE
            }
            else {
                //如果需要刷新，将旧的列表清空
                if (refresh) {
                    adapter.replyList.clear()
                }
                //否则将结果加入codeGoods，并刷新adapter
                adapter.replyList.addAll(it)
                adapter.notifyDataSetChanged()
                cacheData(adapter.replyList)
            }
            /*}*/
        }, { t, rc ->
            reply_swip.isRefreshing = false
            request = null
            adapter.footerState = LoadMoreAdapter.FOOTER_STATE_FAILED
        })
    }
    fun sentReply(){
      
    }
    override fun onDestroy() {
        super.onDestroy()
        loadReplys(false)
    }

    //本地缓存
    fun cacheData(data: List<Reply>) {
        data.forEach {
            it.save()
        }
    }

    fun getCacheData() = Select()
            .from(Reply::class.java)
            .orderBy(Reply_Table.createat, false)
            .queryList()
}
