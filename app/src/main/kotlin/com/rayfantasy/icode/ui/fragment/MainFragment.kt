package com.rayfantasy.icode.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.rayfantasy.icode.R
import com.rayfantasy.icode.postutil.CodeGood
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.extension.fromJson
import com.rayfantasy.icode.ui.adapter.CodeListAdapter
import com.rayfantasy.icode.ui.adapter.LoadMoreAdapter
import kotlinx.android.synthetic.main.fragment_main.view.*
import org.apache.commons.collections4.list.SetUniqueList
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.support.v4.onRefresh
import java.util.*

class MainFragment : FragmentBase() {
    private lateinit var adapter: CodeListAdapter
    private var isRefreshing: Boolean = false
    private lateinit var request: Request<out Any>


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

    override fun onDestroyView() {
        super.onDestroyView()
        //回收资源

        PostUtil.cancel(request)
        isRefreshing = false
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.recyclerView.layoutManager = layoutManager
        adapter = CodeListAdapter(activity, SetUniqueList.setUniqueList(getCacheData())) { loadCodeGoods(true) }
        view.recyclerView.adapter = adapter
        loadCodeGoods(false)
    }

    private fun loadCodeGoods(refresh: Boolean) {
        //如果正在刷新，则不再发起新的刷新请求
        if (isRefreshing)
            return
        isRefreshing = true

        //生成加载条件，目前加载3个，方便测试

        val condition = "${if (!refresh && adapter.codeGoods.isNotEmpty()) "WHERE updateat < ${adapter.codeGoods.last().updateat} " else ""}" +
                "ORDER BY updateat DESC LIMIT 0, 10"
        request = PostUtil.selectCodeGood(condition, {
            view.swipe_layout.isRefreshing = false
            isRefreshing = false

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
            /*view.swipe_layout.isRefreshing = false*/
            isRefreshing = false
            adapter.footerState = LoadMoreAdapter.FOOTER_STATE_FAILED
        })
    }

    //本地缓存
    fun cacheData(data: List<CodeGood>) {
        val dataJson = PostUtil.gson.toJson(data)
        defaultSharedPreferences.edit().putString("cacheData", dataJson).apply()
    }


    fun getCacheData(): List<CodeGood> {
        try {
            var data = defaultSharedPreferences.getString("cacheData", null)
            return PostUtil.gson.fromJson<List<CodeGood>>(data)
        } catch(e: Exception) {
            return ArrayList()
        }
    }

}