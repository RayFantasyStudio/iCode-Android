package com.rayfantasy.icode.ui.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.android.volley.Request
import com.raizlabs.android.dbflow.sql.language.Select
import com.rayfantasy.icode.R
import com.rayfantasy.icode.databinding.ActivityReplyBinding
import com.rayfantasy.icode.extension.string
import com.rayfantasy.icode.extra.PreloadLinearLayoutManager
import com.rayfantasy.icode.model.ICodeTheme
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.Reply
import com.rayfantasy.icode.postutil.bean.Reply_Table
import com.rayfantasy.icode.postutil.extension.e
import com.rayfantasy.icode.ui.adapter.LoadMoreAdapter
import com.rayfantasy.icode.ui.adapter.ReplyListAdapter
import kotlinx.android.synthetic.main.activity_reply.*
import org.apache.commons.collections4.list.SetUniqueList
import org.jetbrains.anko.onClick
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.toast

class ReplyActivity : ActivityBase() {
    companion object {
        const val LOAD_ONCE = 10
    }

    private var id: Int = 1
    private lateinit var adapter: ReplyListAdapter
    private val isRefreshing: Boolean
        get() = request != null
    private var request: Request<*>? = null

    override val bindingStatus: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityReplyBinding>(this, R.layout.activity_reply).theme = ICodeTheme
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        id = intent.getIntExtra("id", 0)
        reply_swip?.onRefresh {
            loadReplys(true)
        }

        initRecyclerView()
        loadReplys(true)

        if (PostUtil.user == null)
            setReplyable(false)

        btn_sent.onClick {
            val reply = Reply(reply_sent_context.string, id)
            setReplyable(false)
            PostUtil.addReply(reply, {
                reply_sent_context.string = ""
                toast("Success")
                setReplyable(true)
                loadReplys(true)
            }, { t, rc ->
                toast("Failed, rc = $rc")
                e("rc = $rc")
                setReplyable(true)
            })
        }
    }

    private fun setReplyable(replyable: Boolean) {
        reply_sent_context.isEnabled = replyable
        btn_sent.isEnabled = replyable
    }

    private fun initRecyclerView() {
        reply_recyclerview.layoutManager = PreloadLinearLayoutManager(this)
        adapter = ReplyListAdapter(this, SetUniqueList.setUniqueList(getCacheData())) { loadReplys(false) }
        reply_recyclerview.adapter = adapter

    }

    private fun loadReplys(refresh: Boolean) {
        //如果正在刷新，则不再发起新的刷新请求
        if (isRefreshing)
            return

        //生成加载条件，目前加载5个，方便测试

        val condition = "WHERE ${if (!refresh && adapter.replyList.isNotEmpty()) "createat < ${adapter.replyList.last().createAt} AND " else ""}good_id=$id " +
                "ORDER BY createat DESC LIMIT 0, $LOAD_ONCE"
        request = PostUtil.findReply(condition, {
            reply_swip.isRefreshing = false
            request = null

            if (it.isEmpty() ) {
                //如果结果为空，则表示没有更多内容了
                adapter.footerState = LoadMoreAdapter.FOOTER_STATE_NO_MORE
            } else {
                //如果需要刷新，将旧的列表清空
                if (refresh) {
                    adapter.replyList.clear()
                }
                //否则将结果加入codeGoods，并刷新adapter
                adapter.replyList.addAll(it)
                if(refresh) adapter.notifyDataSetChanged()
                else adapter.notifyItemRangeInserted(adapter.itemCount -1 - it.size,it.size)
                cacheData(adapter.replyList)
            }
        }, { t, rc ->
            reply_swip.isRefreshing = false
            request = null
            adapter.footerState = LoadMoreAdapter.FOOTER_STATE_FAILED
        })
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
            .where(Reply_Table.goodId.`is`(id))
            .orderBy(Reply_Table.createat, false)
            .queryList()

}


