package com.rayfantasy.icode.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.raizlabs.android.dbflow.sql.language.Join
import com.raizlabs.android.dbflow.sql.language.NameAlias
import com.raizlabs.android.dbflow.sql.language.Select
import com.rayfantasy.icode.R
import com.rayfantasy.icode.extra.UpdateAbleList
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.CodeGood
import com.rayfantasy.icode.postutil.bean.CodeGood_Table
import com.rayfantasy.icode.postutil.bean.Favorite
import com.rayfantasy.icode.postutil.bean.Favorite_Table
import com.rayfantasy.icode.ui.adapter.CodeListAdapter
import com.rayfantasy.icode.ui.adapter.LoadMoreAdapter
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import org.jetbrains.anko.support.v4.onRefresh

class FavoriteFragment : FragmentBase() {
    companion object {
        const val LOAD_ONCE = 10
    }

    private val adapter by lazy { CodeListAdapter(activity, UpdateAbleList(getCacheData())) { loadCodeGoods(false) } }
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
        return inflater?.inflate(R.layout.fragment_favorite, container, false)
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(activity)
        view.favorite_recycler_view.layoutManager = layoutManager
        view.favorite_recycler_view.adapter = adapter
    }

    private fun loadCodeGoods(refresh: Boolean) {
        //如果正在刷新，则不再发起新的刷新请求
        if (isRefreshing)
            return

        //生成加载条件，目前加载3个，方便测试
        if (PostUtil.user == null) {
            adapter.footerState = LoadMoreAdapter.FOOTER_STATE_FAILED
            return
        }
        val condition = "JOIN (SELECT * FROM favorite WHERE userId = ${PostUtil.user!!.id}) b on a.id = b.goodId ORDER BY b.createat DESC"
        request = PostUtil.selectCodeGood(condition) {
            onSuccess {
                if (isDetached) return@onSuccess
                view.favo_swipe.isRefreshing = false
                request = null

                //if (it.isEmpty() ) {
                    //如果结果为空，则表示没有更多内容了
                    adapter.footerState = LoadMoreAdapter.FOOTER_STATE_NO_MORE
                //} else {
                    if (refresh) {
                        adapter.codeGoods.clear()
                    }
                    //否则将结果加入codeGoods，并刷新adapter
                    adapter.codeGoods.addAll(it)
                    if (refresh) adapter.notifyDataSetChanged()
                    else adapter.notifyDataSetChanged()
                    cacheData(adapter.codeGoods)
                //}
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

    fun getCacheData() = Select()
            .from(CodeGood::class.java).`as`("a")
            .join(Favorite::class.java, Join.JoinType.CROSS).`as`("b")
            .on(CodeGood_Table.id.withTable(NameAlias("a")).eq(Favorite_Table.goodId.withTable(NameAlias("b"))))
            .where(CodeGood_Table.id.withTable(NameAlias("a")).`is`(PostUtil.user!!.id))
            .orderBy(Favorite_Table.createat, false)
            .queryList()

}
