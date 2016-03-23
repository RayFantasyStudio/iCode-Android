package com.rayfantasy.icode.ui.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
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
import kotlinx.android.synthetic.main.fragment_account_code.view.*
import org.apache.commons.collections4.list.SetUniqueList
/*这是用户界面用于加载用户发布过代码的fragment
*
 */

class AccountCodeFragment : FragmentBase() {

    companion object {
        const val LOAD_ONCE = 10
    }


    private lateinit var adapter: CodeListAdapter
    private val isRefreshing: Boolean
        get() = request != null
    private var request: Request<*>? = null



    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        loadCodeGoods(true)
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(activity)
        view.code_rv_fragment.layoutManager = layoutManager
        adapter = CodeListAdapter(activity, SetUniqueList.setUniqueList(getCacheData())) { loadCodeGoods(false) }
        view.code_rv_fragment.adapter = adapter
    }

    private fun loadCodeGoods(refresh: Boolean) {
        //如果正在刷新，则不再发起新的刷新请求
        if (isRefreshing)
            return

        //生成加载条件，目前加载3个，方便测试

        val condition = "WHERE ${if (!refresh && adapter.codeGoods.isNotEmpty()) "createat < ${adapter.codeGoods.last().createAt} AND " else ""}username=\"${PostUtil.user!!.username}\" " +
                "ORDER BY createat DESC LIMIT 0, $LOAD_ONCE"
        request = PostUtil.selectCodeGood(condition) {
            onSuccess {
                if (isDetached) return@onSuccess
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
            .where(CodeGood_Table.username.`is`(PostUtil.user!!.username))
            .orderBy(CodeGood_Table.updateat, false)
            .queryList()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_account_code, container, false)
    }


}
