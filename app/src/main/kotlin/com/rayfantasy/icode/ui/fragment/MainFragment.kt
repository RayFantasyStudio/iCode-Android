package com.rayfantasy.icode.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.raizlabs.android.dbflow.sql.language.Select
import com.rayfantasy.icode.R
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.CodeGood
import com.rayfantasy.icode.postutil.bean.CodeGood_Table
import com.rayfantasy.icode.ui.adapter.CodeListAdapter
import com.rayfantasy.icode.ui.adapter.LoadMoreAdapter
import kotlinx.android.synthetic.main.fragment_main.view.*
import org.apache.commons.collections4.list.SetUniqueList
import org.jetbrains.anko.support.v4.onRefresh

class MainFragment : FragmentBase() {
    private lateinit var adapter: CodeListAdapter
    private val isRefreshing: Boolean
        get() = request != null
    private var request: Request<*>? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view?.swipe_layout?.onRefresh {
            loadCodeGoods(true)
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.recyclerView.layoutManager = layoutManager
        adapter = CodeListAdapter(activity, SetUniqueList.setUniqueList(getCacheData())) { loadCodeGoods(true) }
        view.recyclerView.adapter = adapter
    }

    private fun loadCodeGoods(refresh: Boolean) {
        //如果正在刷新，则不再发起新的刷新请求
        if (isRefreshing)
            return

        //生成加载条件，目前加载3个，方便测试

        val condition = "${if (!refresh && adapter.codeGoods.isNotEmpty()) "WHERE updateat < ${adapter.codeGoods.last().updateAt} " else ""}" +
                "ORDER BY updateat DESC LIMIT 0, 10"
        request = PostUtil.selectCodeGood(condition, {
            view.swipe_layout.isRefreshing = false
            request = null

            if (it.isEmpty() ) {
                //如果结果为空，则表示没有更多内容了
                adapter.footerState = LoadMoreAdapter.FOOTER_STATE_NO_MORE
            }
            /*  if (it.size <=10 && it.isNotEmpty()){


                  adapter.setFooterState(CodeListAdapter.FOOTER_STATE_NO_MORE)
              }*/
            else {
                //如果需要刷新，将旧的列表清空

                /*
                                if (it.size <= 10 && !it.isEmpty()) {
                                    adapter.notifyDataSetChanged()
                                    adapter.setFooterState(CodeListAdapter.FOOTER_STATE_NO_MORE)
                                } else {*/
                if (refresh) {
                    adapter.codeGoods.clear()
                }
                //否则将结果加入codeGoods，并刷新adapter
                adapter.codeGoods.addAll(it)
                adapter.notifyDataSetChanged()
                cacheData(adapter.codeGoods)
            }
            /*}*/
        }, { t, rc ->
            view.swipe_layout.isRefreshing = false
            request = null
            adapter.footerState = LoadMoreAdapter.FOOTER_STATE_FAILED
        })
    }

    override fun onStart() {
        super.onStart()
        loadCodeGoods(false)
    }

    override fun onStop() {
        super.onStop()
        PostUtil.cancel(request)
        request = null
    }

    //本地缓存
    fun cacheData(data: List<CodeGood>) {
        data.forEach {
            it.save()
        }
    }

    fun getCacheData() = Select()
            .from(CodeGood::class.java)
            .orderBy(CodeGood_Table.updateat, false)
            .queryList()
}
