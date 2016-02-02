package com.rayfantasy.icode.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.rayfantasy.icode.R
import com.rayfantasy.icode.extension.inflate
import com.rayfantasy.icode.postutil.PostUtil
import com.rayfantasy.icode.postutil.bean.CodeGood
import com.rayfantasy.icode.ui.activity.BlocksActivity
import com.rayfantasy.icode.util.ms2RelativeDate
import kotlinx.android.synthetic.main.item_recycler_code_list.view.*
import kotlinx.android.synthetic.main.item_recycler_user.view.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.footer_recycler_view.view.*

/**
 * Created by qweas on 2016/1/22 0022.
 */
class UserListAdapter(val activity: Activity, var codeGoods: MutableList<CodeGood>,private  val  onLoadingMore: () -> Unit ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var footerState: Int = 0
    private var hintNoMore = R.string.footer_msg_no_more
    private val footerViewHolder: FooterViewHolder
     companion object {
        const val VIEW_TYPE_HEADER = -1
        const val VIEW_TYPE_NORMAL = -2
        val FOOTER_STATE_LOADING = 0
        val FOOTER_STATE_NO_MORE = 1
        val FOOTER_STATE_FAILED = 2
        val FOOTER_STATE_HIDDEN = 3
        private val VIEW_TYPE_FOOTER = 1
    }

    override fun getItemCount() = codeGoods.size + 1
    init {
        @SuppressLint("InflateParams")
        val footerView = LayoutInflater.from(activity).inflate(R.layout.footer_recycler_view, null, false)
        footerView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        footerViewHolder = FooterViewHolder(footerView)
        footerView.onClick {
            if (footerState == FOOTER_STATE_LOADING) return@onClick
            setFooterState(FOOTER_STATE_LOADING)
            onLoadingMore()
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {

        when (holder) {
            is UserViewHolder -> {
                var username: String = PostUtil.user!!.username
                Glide.with(activity)
                        .load(PostUtil.getProfilePicUrl(username))
                        .error(R.mipmap.ic_user_black)
                        .bitmapTransform(CropCircleTransformation(activity))
                        .into(holder.usericon)
                holder.username.text = username

            }
            is CodeViewHolder -> {
                val codeGood = codeGoods[position - 1]

                holder.title.text = codeGood.title
                holder.subTitle.text = codeGood.subtitle
                holder.time.text = ms2RelativeDate(activity, codeGood.createAt!!)
                holder.username.text = codeGood.username
                if (codeGood.highlight ?: false) {
                    holder.title.setTextColor(Color.RED)
                    holder.username.setTextColor(Color.RED)
                    holder.subTitle.setTextColor(Color.RED)
                    holder.username.append("被管理员临时高亮!")
                } else {
                    holder.title.setTextColor(Color.rgb(0, 136, 255))
                    holder.username.setTextColor(Color.rgb(140, 140, 140))
                    holder.subTitle.setTextColor(Color.rgb(140, 140, 140))
                }
                Glide.with(activity)
                        .load(PostUtil.getProfilePicUrl(codeGood.username!!))
                        .error(R.mipmap.ic_user_black)
                        .bitmapTransform(CropCircleTransformation(activity))
                        .into(holder.pic)
                holder.bg.onClick {
                    activity.startActivity<BlocksActivity>("codeGood" to codeGood)
                }
            }
            is FooterViewHolder ->{
                setFooterState(FOOTER_STATE_LOADING)
                onLoadingMore()
            }
        }
    }

    override fun getItemViewType(position: Int)
            = if (position == 0) VIEW_TYPE_HEADER else if (position == getItemCount() + 2) VIEW_TYPE_FOOTER else VIEW_TYPE_NORMAL

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType){
        VIEW_TYPE_HEADER -> UserViewHolder(parent.inflate(R.layout.item_recycler_user))
        VIEW_TYPE_NORMAL -> CodeViewHolder(parent.inflate(R.layout.item_recycler_code_list))
        VIEW_TYPE_FOOTER -> FooterViewHolder(parent.inflate(R.layout.footer_recycler_view))
        else -> null
    }
    fun setFooterState(footerState: Int) {
        this.footerState = footerState
        when (footerState) {
            FOOTER_STATE_HIDDEN -> footerViewHolder.root.visibility = View.GONE

            FOOTER_STATE_LOADING -> {
                footerViewHolder.root.visibility = View.VISIBLE
                footerViewHolder.progress.visibility = View.VISIBLE
                footerViewHolder.msg.setText(R.string.footer_msg_loading)
            }

            FOOTER_STATE_NO_MORE -> {
                footerViewHolder.root.visibility = View.VISIBLE
                footerViewHolder.progress.visibility = View.GONE
                footerViewHolder.msg.setText(hintNoMore)
            }

            FOOTER_STATE_FAILED -> {
                footerViewHolder.root.visibility = View.VISIBLE
                footerViewHolder.progress.visibility = View.GONE
                footerViewHolder.msg.setText(R.string.footer_msg_failed)
            }
        }

    }
    fun setHintNoMore(hintNoMore: Int) {
        this.hintNoMore = hintNoMore
    }
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var username = itemView.user_item_username
        var usericon = itemView.user_item_avatar
    }

    class CodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pic = itemView.pic
        val username = itemView.username
        val time = itemView.time
        val title = itemView.title
        val subTitle = itemView.sub_title
        val bg = itemView.element_bg
    }

    class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val root = itemView.footer_root
        val msg = itemView.footer_msg
        val progress = itemView.footer_progress
    }


}