package com.rayfantasy.icode.ui.activity

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.android.volley.Request
import com.rayfantasy.icode.R
import com.rayfantasy.icode.database.DBHelper
import com.rayfantasy.icode.extension.onPanelSlide
import com.rayfantasy.icode.postutil.CodeGood
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.Reply
import com.rayfantasy.icode.postutil.extension.fromJson
import com.rayfantasy.icode.ui.adapter.LoadMoreAdapter
import com.rayfantasy.icode.ui.adapter.ReplyListAdapter
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.content_view_code.*
import kotlinx.android.synthetic.main.sliding_up_panel_body.*
import kotlinx.android.synthetic.main.sliding_up_panel_footer.*
import org.apache.commons.collections4.list.SetUniqueList
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.inputMethodManager
import org.jetbrains.anko.onClick
import java.util.*

class TestActivity : ActivityBase() {
    private lateinit var adapter: ReplyListAdapter
    private var isRefreshing: Boolean = false
    private lateinit var request: Request<out Any>
    private lateinit var codeGood: CodeGood
    var database: DBHelper = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        tv_code.isEnabled = false
        tv_code.isFocusable = false
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        reply_recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        codeGood = intent.getSerializableExtra("codeGood") as CodeGood
        title = codeGood.title
        adapter = ReplyListAdapter(this, SetUniqueList.setUniqueList(getCacheData()), { replyRefresh(true) })


        tv_code.setText(codeGood.content.toString())
        view_code_tv_sub.text = codeGood.subtitle.toString()

        replyRefresh(true)
        view_code_btn_send.onClick {
            /* PostUtil.addReply(Reply(view_code_tv_send.text.toString(), codeGood.id!!.toInt(), codeGood.username),
                     onSuccess = {
                         Toast.makeText(this, "发表评论成功!", Toast.LENGTH_SHORT)
                     },
                     onFailed = { t, rc ->
                         e("failed, rc =  $rc")
                         Toast.makeText(this, "发表失败", Toast.LENGTH_LONG).show()
                     }
             )*/
            var db: SQLiteDatabase = database.readableDatabase
            db.execSQL("insert into favo(codeGoodId,title,sub_title,name,code,createat,updateat values(${codeGood.id},${codeGood.title},${codeGood.subtitle},${codeGood.username},${codeGood.content},${codeGood.createat},${codeGood.updateat})")
        }
        toolbar.setLogo(R.mipmap.ic_launcher)

        sliding_layout.onPanelSlide {
            onPanelCollapsed { inputMethodManager.hideSoftInputFromWindow(view_code_tv_send.windowToken, 0) }
        }


    }

    fun replyRefresh(refresh: Boolean) {
        if (isRefreshing)
            return
        isRefreshing = true

        val condition = "WHERE good_id = ${codeGood.id}"
        request = PostUtil.findReply(condition, {
            /*adapter = ReplyListAdapter(this, it) { replyRefresh(true) }*/
            this.reply_list_swipe.isRefreshing = false
            reply_recyclerView.adapter = adapter
            isRefreshing = false
            if (it.isEmpty()) {
                adapter.footerState = LoadMoreAdapter.FOOTER_STATE_NO_MORE
            } else {
                if (refresh) {
                    adapter.replyList.clear()
                }
                adapter.replyList.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }, { t, rc ->
            this.reply_list_swipe.isRefreshing = false
            isRefreshing = false
            adapter.footerState = LoadMoreAdapter.FOOTER_STATE_FAILED
            cacheData(adapter.replyList)
        })


    }


    override fun onDestroy() {
        super.onDestroy()
        PostUtil.cancel(request)
        isRefreshing = false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (sliding_layout.isOverlayed == true) {
            sliding_layout.isOverlayed == false
        }
    }

    //本地缓存
    fun cacheData(data: List<Reply>) {
        val dataJson = PostUtil.gson.toJson(data)
        defaultSharedPreferences.edit().putString("reply_cacheData", dataJson).apply()
    }


    fun getCacheData(): List<Reply> {
        try {
            var data = defaultSharedPreferences.getString("reply_cacheData", null)
            return PostUtil.gson.fromJson<List<Reply>>(data)
        } catch(e: Exception) {
            return ArrayList()
        }
    }
}
