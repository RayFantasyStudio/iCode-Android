package com.rayfantasy.icode.ui.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.raizlabs.android.dbflow.sql.language.Select

import com.rayfantasy.icode.R
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.Reply
import com.rayfantasy.icode.postutil.bean.Reply_Table
import com.rayfantasy.icode.ui.adapter.LoadMoreAdapter
import com.rayfantasy.icode.ui.adapter.ReplyListAdapter
import kotlinx.android.synthetic.main.activity_reply.*
import kotlinx.android.synthetic.main.fragment_account_reply.view.*

import org.apache.commons.collections4.list.SetUniqueList

/**
 * A simple [Fragment] subclass.
 */
class ReplyFragment : FragmentBase() {
    companion object {
        const val LOAD_ONCE = 10
    }
    private var reply_count = 0
    private lateinit var adapter: ReplyListAdapter
    private val isRefreshing: Boolean
        get() = request != null
    private var request: Request<*>? = null
    private val replyList by lazy { SetUniqueList.setUniqueList(getCacheData()) }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_account_reply, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        initRecyclerView()
        loadReplys(true)
    }
    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(activity)
        view.reply_rv_fragment.layoutManager = layoutManager
        adapter = ReplyListAdapter(activity, SetUniqueList.setUniqueList(getCacheData())) { loadReplys(false) }
        view.reply_rv_fragment.adapter = adapter

    }
    private fun loadReplys(refresh: Boolean) {
        //如果正在刷新，则不再发起新的刷新请求
        if (isRefreshing)
            return

        //生成加载条件，目前加载5个，方便测试

        val condition = "WHERE ${if (!refresh && replyList.isNotEmpty()) "createat < ${replyList.last().createAt} AND " else ""}username=\"${PostUtil.user!!.username}\" " +
                "ORDER BY createat DESC LIMIT 0, $LOAD_ONCE"
        request = PostUtil.findReply(condition) {
            onSuccess {
                request = null
                //如果需要刷新，将旧的列表清空
                if (refresh) {
                    replyList.clear()
                }
                //否则将结果加入codeGoods
                replyList.addAll(it)
                cacheData(it)
                    if (it.isEmpty() ) {
                        //如果结果为空，则表示没有更多内容了
                        adapter.footerState = LoadMoreAdapter.FOOTER_STATE_NO_MORE
                    } else {
                        if (refresh) adapter.notifyDataSetChanged()

                    }
            }
            onFailed { t, rc ->
                request = null
                Toast.makeText(activity,"$rc", Toast.LENGTH_SHORT).show()
                adapter.footerState = LoadMoreAdapter.FOOTER_STATE_FAILED
            }
        }
    }


    //本地缓存
    fun cacheData(data: List<Reply>) {
        data.forEach {
            it.save()
        }
    }

    fun getCacheData() = Select()
            .from(Reply::class.java)
            .where(Reply_Table.username.`is`(PostUtil.user!!.username))
            .orderBy(Reply_Table.createat, false)
            .queryList()


}// Required empty public constructor
