package com.rayfantasy.icode.ui.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.rayfantasy.icode.postutil.bean.CodeGood
import kotlinx.android.synthetic.main.item_recycler_code_list.view.*
import kotlinx.android.synthetic.main.item_recycler_user.view.*


/**
 * Created by qweas on 2016/1/22 0022.
 */
class UserListAdapter(val activity: Activity, var codeGoods: MutableList<CodeGood>, onLoadingMore: () -> Unit) :
        LoadMoreAdapter<UserListAdapter.ViewHolder_code>(activity, onLoadingMore) {
    override fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): ViewHolder_code {
        throw UnsupportedOperationException()
    }

    override val normalItemCount: Int
        get() = codeGoods.size + 1


    override fun onBindNormalViewHolder(holder: ViewHolder_code, position: Int) {
        throw UnsupportedOperationException()
    }


    class ViewHolder_user(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var user_avatar = itemView.user_item_avatar
        var username = itemView.user_item_username
    }

    class ViewHolder_code(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pic = itemView.pic
        val username = itemView.username
        val time = itemView.time
        val title = itemView.title
        val subTitle = itemView.sub_title
        val bg = itemView.element_bg
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
}