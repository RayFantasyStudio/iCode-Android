package com.rayfantasy.icode.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.rayfantasy.icode.R
import com.rayfantasy.icode.databinding.FragmentMainBinding
import com.rayfantasy.icode.model.ICodeTheme
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.CodeGood
import com.rayfantasy.icode.postutil.bean.CodeGood_Table
import com.rayfantasy.icode.postutil.bean.Favorite
import com.rayfantasy.icode.postutil.bean.Favorite_Table
import com.rayfantasy.icode.ui.adapter.CodeListAdapter
import com.rayfantasy.icode.ui.adapter.LoadMoreAdapter
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import org.apache.commons.collections4.list.SetUniqueList
import org.jetbrains.anko.support.v4.onRefresh

class FavoriteFragment : FragmentBase() {
    companion object {
        const val LOAD_ONCE = 10
    }
    private lateinit var adapter: CodeListAdapter
    private val isRefreshing: Boolean
        get() = request != null
    private var request: Request<*>? = null
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view?.favo_swipe?.onRefresh { loadCodeGoods(true) }
        initRecyclerView()
        loadCodeGoods(true)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_favorite,container,false)
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(activity)
        view.favorite_recycler_view.layoutManager = layoutManager
        adapter = CodeListAdapter(activity, SetUniqueList.setUniqueList(getCacheData())) { loadCodeGoods(false) }
        view.favorite_recycler_view.adapter = adapter
    }
    private fun loadCodeGoods(refresh: Boolean) {
        //如果正在刷新，则不再发起新的刷新请求
        if (isRefreshing)
            return

        //生成加载条件，目前加载3个，方便测试
        if(PostUtil.user == null){
            adapter.footerState = LoadMoreAdapter.FOOTER_STATE_FAILED
            return
        }
        val condition = "SELECT a.* FROM icode.code_good a JOIN (SELECT * FROM icode.favorite WHERE userId = ${PostUtil.user!!.id}) b on a.id = b.goodId ORDER BY b.createat"
        request = PostUtil.selectCodeGood(condition) {
            onSuccess {
                if (isDetached) return@onSuccess
                view.favo_swipe.isRefreshing = false
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
                    view.favo_swipe.isRefreshing = false
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
    fun cacheData(data: List<CodeGood>) {
        data.forEach {
            it.loadContentFromCache()
            it.save()
        }
    }
    fun getCacheData() = SQLite.select(CodeGood_Table.id)
            .from(CodeGood::class.java).leftOuterJoin(Favorite::class.java)
            .on(CodeGood_Table.id.withTable().eq(Favorite_Table.goodId.withTable()))
            .queryList()

}
