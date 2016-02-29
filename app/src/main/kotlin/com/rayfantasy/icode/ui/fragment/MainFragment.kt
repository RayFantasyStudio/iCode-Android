package com.rayfantasy.icode.ui.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.raizlabs.android.dbflow.sql.language.Select
import com.rayfantasy.icode.databinding.FragmentMainBinding
import com.rayfantasy.icode.extra.PreloadLinearLayoutManager
import com.rayfantasy.icode.model.ICodeTheme
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.CodeGood
import com.rayfantasy.icode.postutil.bean.CodeGood_Table
import com.rayfantasy.icode.ui.activity.AccountActivity
import com.rayfantasy.icode.ui.activity.WriteCodeActivity
import com.rayfantasy.icode.ui.activity.startFabTransformActivity
import com.rayfantasy.icode.ui.adapter.CodeListAdapter
import com.rayfantasy.icode.ui.adapter.LoadMoreAdapter
import com.rayfantasy.icode.util.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import org.apache.commons.collections4.list.SetUniqueList
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startService
import org.jetbrains.anko.support.v4.onRefresh

class MainFragment : FragmentBase() {
    private lateinit var broadcastManager: LocalBroadcastManager
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
        }
    }

    companion object {
        const val LOAD_ONCE = 10
    }

    private lateinit var adapter: CodeListAdapter
    private val isRefreshing: Boolean
        get() = request != null
    private var request: Request<*>? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.theme = ICodeTheme
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view?.swipe_layout?.onRefresh {
            loadCodeGoods(true)
        }
        broadcastManager = LocalBroadcastManager.getInstance(activity)

        initRecyclerView()
        loadCodeGoods(true)
        fab_main.onClick {/* fab_main.startFabTransformActivity<WriteCodeActivity>()*/ startActivity<AccountActivity>() }
        recyclerView.addItemDecoration(SpaceItemDecoration())
    }

    private fun initRecyclerView() {
        val layoutManager = PreloadLinearLayoutManager(activity)
        view.recyclerView.layoutManager = layoutManager
        adapter = CodeListAdapter(activity, SetUniqueList.setUniqueList(getCacheData())) { loadCodeGoods(false) }
        view.recyclerView.adapter = adapter
    }

    private fun loadCodeGoods(refresh: Boolean) {
        //如果正在刷新，则不再发起新的刷新请求
        if (isRefreshing)
            return

        //生成加载条件，目前加载3个，方便测试

        val condition = "${if (!refresh && adapter.codeGoods.isNotEmpty()) "WHERE updateat < ${adapter.codeGoods.last().updateAt} " else ""}" +
                "ORDER BY updateat DESC LIMIT 0, $LOAD_ONCE"
        request = PostUtil.selectCodeGood(condition) {
            onSuccess {
                if (isDetached) return@onSuccess
                view.swipe_layout.isRefreshing = false
                request = null

                if (it.isEmpty() ) {
                    //如果结果为空，则表示没有更多内容了
                    adapter.footerState = LoadMoreAdapter.FOOTER_STATE_NO_MORE
                } else {
                    if (refresh) {
                        adapter.codeGoods.clear()
                    }
                    //否则将结果加入codeGoods，并刷新adapter
                    adapter.codeGoods.addAll(it)
                    if (refresh) adapter.notifyDataSetChanged()
                    else adapter.notifyItemRangeInserted(adapter.itemCount - 1 - it.size, it.size)
                    cacheData(adapter.codeGoods)
                }
                onFailed { t, rc ->
                    request = null
                    if (isDetached || view == null) return@onFailed
                    view.swipe_layout.isRefreshing = false
                    adapter.footerState = LoadMoreAdapter.FOOTER_STATE_FAILED
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        PostUtil.cancel(request)
        request = null
    }

    //本地缓存
    fun cacheData(data: List<CodeGood>) {
        data.forEach {
            it.loadContentFromCache()
            it.save()
        }
    }

    fun getCacheData() = Select()
            .from(CodeGood::class.java)
            .orderBy(CodeGood_Table.updateat, false)
            .queryList()
}
